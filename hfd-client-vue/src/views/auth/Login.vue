<template>
    <FloatingConfigurator />
    <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
        <div class="flex flex-col items-center justify-center">
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%); width: 480px; max-width: 95vw;">
                <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px; overflow: hidden; word-break: break-word;">
                    <div class="text-center mb-8">
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">{{ name }}</div>
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">초과근무 관리</div>
                        <!--<span class="text-muted-color font-medium">Sign in to continue</span>-->
                    </div>

                    <!-- 회원가입 완료 안내 메시지 -->
                    <Message v-if="isRegistered" severity="success" class="mb-5 w-full" :closable="true" @close="isRegistered = false">
                        <span class="font-medium">회원가입이 완료되었습니다!</span>
                        <span class="ml-1 text-600">로그인해 주세요.</span>
                    </Message>
                    <!--2026-03-07-->
                    <!--<Message v-if="isRegistered" severity="success" class="mb-4 w-full" :closable="false"> 회원가입이 완료되었습니다. 로그인해주세요 🎉 </Message>-->

                    <Message v-if="isResetSuccess" severity="success" class="mb-4 w-full" :closable="false"> 비밀번호가 재설정되었습니다. 새 비밀번호로 로그인해주세요 🔐 </Message>

                    <!-- 로그인 에러 메시지 -->
                    <Message v-if="errorMsg" severity="error" class="mb-5 w-full" :closable="true" @close="errorMsg = ''">
                        {{ errorMsg }}
                    </Message>

                    <!-- 로그인 폼 -->
                    <form @submit.prevent="handleLogin" class="login-form">
                        <!-- 이메일 (accountId) -->
                        <div class="field mb-5">
                            <label for="accountId" class="font-medium text-500 text-sm mb-2 block"> 이메일 (계정 ID) </label>

                            <InputGroup>
                                <InputGroupAddon>
                                    <i class="pi pi-envelope"></i>
                                </InputGroupAddon>
                                <InputText
                                    id="accountId"
                                    v-model="form.accountId"
                                    type="email"
                                    placeholder="example@email.com"
                                    class="w-full"
                                    :invalid="!!fieldErrors.accountId"
                                    :disabled="loading"
                                    autocomplete="username"
                                    @input="fieldErrors.accountId = ''"
                                />
                            </InputGroup>
                            <div>
                            <small v-if="fieldErrors.accountId" 
                                class="p-error block mt-1">
                                {{ fieldErrors.accountId }}
                            </small>
                            </div>
                        </div>

                        <!-- 비밀번호 -->
                        <div class="field mb-4">
                            <label for="password" class="font-medium text-500 text-sm mb-2 block"> 비밀번호 </label>
                            <InputGroup>
                                <InputGroupAddon>
                                    <i class="pi pi-lock"></i>
                                </InputGroupAddon>
                                <Password
                                    id="password"
                                    v-model="form.password"
                                    placeholder="비밀번호 입력"
                                    :feedback="false"
                                    toggleMask
                                    class="w-full"
                                    inputClass="w-full"
                                    :invalid="!!fieldErrors.password"
                                    :disabled="loading"
                                    autocomplete="current-password"
                                    @input="fieldErrors.password = ''"
                                />
                            </InputGroup>

                            <small v-if="fieldErrors.password" class="p-error block mt-1">
                                {{ fieldErrors.password }}
                            </small>
                        </div>

                        <!-- ✅ 자동 로그인(왼쪽) + 비밀번호 찾기(오른쪽) 양쪽 정렬 -->
                        <div class="flex align-items-center justify-content-between w-full mb-6">
                            <!-- 왼쪽: 자동 로그인 -->
                            <div class="flex align-items-center gap-2">
                                <Checkbox v-model="rememberMe" inputId="remember" binary :disabled="loading" />
                                <label for="remember" class="text-500 text-sm cursor-pointer select-none"> 이메일 저장 </label>
                            </div>

                            <!-- 오른쪽: 비밀번호 찾기 (ml-auto 추가) -->
                            <router-link 
                                to="/auth/forgot-password" 
                                class="text-primary text-sm font-medium no-underline hover:underline ml-auto">
                                비밀번호 초기화 
                            </router-link>

                        </div>

                        <!-- 로그인 버튼 -->
                        <Button type="submit" label="로그인" icon="pi pi-sign-in" class="w-full mb-3" :loading="loading" :disabled="!canSubmit" size="large" />

                        <!-- 구분선 -->
                        <Divider align="center">
                            <span class="text-400 text-sm px-2">또는</span>
                        </Divider>

                        <!-- 회원가입 이동 -->
                        <div class="text-center mt-3">
                            <span class="text-500 text-sm">계정이 없으신가요?</span>
                            <router-link to="/auth/signup" class="text-primary font-medium text-sm ml-2 no-underline hover:underline"> 회원가입 </router-link>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import FloatingConfigurator from '@/components/FloatingConfigurator.vue';
import { useAuthStore } from '@/stores/auth.js';
import { computed, onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import Button from 'primevue/button';
import Checkbox from 'primevue/checkbox';
import Divider from 'primevue/divider';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import Password from 'primevue/password';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

// ── 상태 ────────────────────────────────────────────────────────────────
const loading = ref(false);
const rememberMe = ref(false);
const errorMsg = ref('');
const isRegistered = ref(false);
const isResetSuccess = ref(false);

const form = reactive({
    accountId: '',
    password: ''
});

const fieldErrors = reactive({
    accountId: '',
    password: ''
});
// ── 마운트 시 초기화 ─────────────────────────────────────────────────────
onMounted(() => {
    // 회원가입 완료 후 리다이렉트 감지
    if (route.query.registered === '1') {
        isRegistered.value = true;
    }

    if (route.query.reset === '1') {
        isResetSuccess.value = true;
    }

    // 자동 로그인 저장된 계정 복원
    const saved = localStorage.getItem('savedAccountId');
    if (saved) {
        form.accountId = saved;
        rememberMe.value = true;
    }
});

// ── 유효성 검사 ──────────────────────────────────────────────────────────
const canSubmit = computed(() => form.accountId.trim().length > 0 && form.password.length > 0 && !loading.value);

function validate() {
    let valid = true;
    fieldErrors.accountId = '';
    fieldErrors.password = '';

    if (!form.accountId.trim()) {
        fieldErrors.accountId = '이메일을 입력해주세요.';
        valid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.accountId)) {
        fieldErrors.accountId = '올바른 이메일 형식을 입력해주세요.';
        valid = false;
    }

    if (!form.password) {
        fieldErrors.password = '비밀번호를 입력해주세요.';
        valid = false;
    }

    return valid;
}

// ── 로그인 처리 ──────────────────────────────────────────────────────────
async function handleLogin() {
    if (!validate()) return;

    errorMsg.value = '';
    loading.value = true;

    try {
        await authStore.login({
            accountId: form.accountId,
            password: form.password
        });

        // 자동 로그인 처리
        if (rememberMe.value) {
            localStorage.setItem('savedAccountId', form.accountId);
        } else {
            localStorage.removeItem('savedAccountId');
        }

        // 로그인 성공 → 이전 페이지 또는 대시보드로 이동
        const redirect = route.query.redirect || '/register/overtime';
        router.push(redirect);
    } catch (err) {
        const status = err.response?.status;
        const message = err.response?.data?.error;

        if (status === 401) {
            errorMsg.value = '이메일 또는 비밀번호가 올바르지 않습니다.';
        } else if (status === 403) {
            errorMsg.value = '계정이 비활성화 상태입니다. 관리자에게 문의해주세요.';
        } else {
            errorMsg.value = message || '로그인 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
        }
    } finally {
        loading.value = false;
    }
}

const name = 'Welcome To Happy Friday';
</script>

<style scoped>
.pi-eye {
    transform: scale(1.6);
    margin-right: 1rem;
}

.pi-eye-slash {
    transform: scale(1.6);
    margin-right: 1rem;
}
</style>
