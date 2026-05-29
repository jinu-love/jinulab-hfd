import AppLayout from '@/layout/AppLayout.vue';
import { createRouter, createWebHistory } from 'vue-router';

/**
 * 라우트 정의
 *
 * meta 옵션:
 *   requiresAuth:  true → 로그인 필요 (미로그인 시 /login 리다이렉트)
 *   requiresAdmin: true → ADMIN 권한 필요 (권한 없으면 /dashboard 리다이렉트)
 *   guest:         true → 비로그인 전용 (로그인 상태에서 접근 시 /dashboard 리다이렉트)
 */


const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/auth/login'  // 루트 접근 시 로그인 페이지로
        },
        {
            path: '/',
            component: AppLayout,
            children: [
                {
                    path: '/',
                    redirect: '/register/overtime'
                },
                {
                    path: '/register/overtime',
                    name: 'registerOvertime',
                    component: () => import('@/views/RegisterOvertime.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/select/overtime',
                    name: 'selectOvertime',
                    component: () => import('@/views/SelectOvertime.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/customer/report',
                    name: 'CustomerReport',
                    component: () => import('@/views/CustomerReport.vue'),
                    meta: { requiresAuth: true }
                },
                // ✅ 추가
                {
                    path: '/profile',
                    name: 'profile',
                    component: () => import('@/views/auth/ProfileView.vue'),
                    meta: { requiresAuth: true }
                }
            ]
        },
        {
            path: '/auth/login',
            name: 'login',
            component: () => import('@/views/auth/Login.vue'),
            meta: {
                guest: true,
                hideNavbar: true
            }
        },
        {
            path: '/auth/signup',
            name: 'signup',
            component: () => import('@/views/auth/SignUpView.vue'),
            meta: {
                guest: true,
                hideNavbar: true
            }
        },
        {
            path: '/auth/forgot-password',
            name: 'forgotPassword',
            component: () => import('@/views/auth/ForgotPasswordView.vue'),
            meta: { guest: true, hideNavbar: true }
        },
        {
            path: '/auth/reset-password',
            name: 'resetPasswordByLink',
            component: () => import('@/views/auth/ResetPasswordLinkView.vue'),
            meta: { guest: true, hideNavbar: true }
        }
    ]
});

/**
 * 전역 네비게이션 가드
 *
 * 모든 페이지 이동 전에 실행
 * 동적 import: 순환 참조 방지
 */
router.beforeEach(async (to) => {
    const { useAuthStore } = await import('@/stores/auth.js')
    const authStore = useAuthStore()

    // ✅ 핵심: 초기화가 완료되지 않았으면 완료될 때까지 대기
    if (!authStore.initialized) {
        await authStore.initializeAuth()
    }

    // 1. 인증 필요 페이지 → 미로그인 시 로그인으로 이동
    //    redirect 파라미터: 로그인 후 원래 페이지로 복귀하기 위해 보존
    if (to.meta.requiresAuth && !authStore.isAuthenticated) {
        return { path: '/auth/login', query: { redirect: to.fullPath } }
    }

    // 2. 관리자 전용 페이지 → ADMIN 권한 없으면 대시보드로
    if (to.meta.requiresAdmin && !authStore.isAdmin) {
        return { path: '/register/overtime' }
    }

    // 3. 게스트 전용 페이지(로그인/회원가입) → 이미 로그인된 경우 대시보드로
    if (to.meta.guest && authStore.isAuthenticated) {
        return { path: '/register/overtime' }
    }

    // 조건 없으면 정상 진행
})

export default router