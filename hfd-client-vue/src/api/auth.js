import axios from 'axios'

/**
 * 인증 불필요 API
 *
 * apiClient 인터셉터를 거치지 않는 순수 axios 사용
 * 이유:
 *   1. 로그인/회원가입은 토큰이 없는 상태에서 호출
 *   2. authAPI.refresh()는 토큰 갱신 인터셉터 내부에서 호출되므로
 *      apiClient를 사용하면 인터셉터 무한루프 발생 → 순수 axios 사용
 */
export const authAPI = {
    login: (data) => axios.post('/api/auth/login', data),
    register: (data) => axios.post('/api/auth/register', data),
    sendCode: (data) => axios.post('/api/auth/send-code', data),
    verifyCode: (data) => axios.post('/api/auth/verify-code', data),
    refresh: (data) => axios.post('/api/auth/refresh', data),  // ← 인터셉터 루프 방지
    logout: (data) => axios.post('/api/auth/logout', data),
    logoutAll: (data) => axios.post('/api/auth/logout/all', data),
    forgotPassword: (data) => axios.post('/api/auth/forgot-password', data),
    resetPassword: (data) => axios.post('/api/auth/reset-password', data),
}

/**
 * 인증 필요 API 공통 인스턴스
 *
 * 모든 보호된 API 요청에 사용
 * Request 인터셉터:  Access Token 자동 첨부
 * Response 인터셉터: 401 감지 → Refresh Token으로 자동 갱신 → 재시도
 */
export const apiClient = axios.create({
    baseURL: '/api',
    //baseURL: 'http://13.237.157.202:8080/api',
    headers: { 'Content-Type': 'application/json' },
})


// ── Request 인터셉터 ──────────────────────────────────────────
/**
 * 모든 요청 전 Access Token을 Authorization 헤더에 자동 첨부
 *
 * 동적 import 사용 이유:
 *   auth.js(api) ↔ auth.js(store) 순환 참조 방지
 *   파일 로드 시점이 아닌 요청 시점에 store를 가져옴
 */
apiClient.interceptors.request.use(async (config) => {
    const { useAuthStore } = await import('@/stores/auth.js')
    const authStore = useAuthStore()

    if (authStore.accessToken) {
        // Bearer 토큰 형식으로 헤더 첨부
        config.headers.Authorization = `Bearer ${authStore.accessToken}`
    }

    return config
})

// ── Response 인터셉터: 토큰 자동 갱신 ────────────────────────

/**
 * 동시에 여러 요청이 401을 받았을 때 refresh를 한 번만 호출하기 위한 상태
 *
 * 예시:
 *   요청 A, B, C 동시 401 발생
 *   → A가 refresh 시작 (isRefreshing = true)
 *   → B, C는 pendingQueue에 대기
 *   → refresh 완료 후 A, B, C 모두 새 토큰으로 재시도
 */
let isRefreshing = false
let pendingQueue = []  // { resolve, reject } 콜백 저장

/**
 * 대기 중인 요청들을 일괄 처리
 * @param {Error|null} error  - 에러 발생 시 reject, 성공 시 null
 * @param {string|null} token - 새로 발급된 Access Token
 */
const flushQueue = (error, token = null) => {
    pendingQueue.forEach(({ resolve, reject }) =>
        error ? reject(error) : resolve(token)
    )
    pendingQueue = []
}

apiClient.interceptors.response.use(
    // 정상 응답은 그대로 통과
    (response) => response,

    async (error) => {
        const originalRequest = error.config
        const status = error.response?.status
        const code = error.response?.data?.code

        // 401이 아니면 그냥 에러 반환
        if (status !== 401) {
            return Promise.reject(error)
        }

        // ✅ 토큰 자체가 유효하지 않으면 refresh 시도 없이 바로 로그아웃
        if (code === 'INVALID_TOKEN') {
            const { useAuthStore } = await import('@/stores/auth.js')
            useAuthStore().clearTokens()
            if (window.location.pathname !== '/auth/login') {
                window.location.href = '/auth/login'
            }
            return Promise.reject(error)
        }

        // 이미 재시도한 요청이면 에러 반환
        if (originalRequest._retry) {
            return Promise.reject(error)
        }

        // // 401이 아니거나 이미 재시도한 요청이면 그냥 에러 반환
        // // _retry 플래그: 재시도 요청이 또 401을 받았을 때 무한루프 방지
        // if (error.response?.status !== 401 || originalRequest._retry) {
        //     return Promise.reject(error)
        // }

        // ── 이미 갱신 중인 경우: 큐에 대기 ───────────────────────
        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                pendingQueue.push({ resolve, reject })
            }).then((newToken) => {
                // 갱신 완료 신호를 받으면 새 토큰으로 원래 요청 재시도
                originalRequest.headers.Authorization = `Bearer ${newToken}`
                return apiClient(originalRequest)
            })
        }

        // ── 토큰 갱신 시작 ────────────────────────────────────────
        originalRequest._retry = true  // 재시도 플래그 설정
        isRefreshing = true

        try {
            const { useAuthStore } = await import('@/stores/auth.js')
            const authStore = useAuthStore()

            if (!authStore.refreshToken) {
                throw new Error('Refresh token이 없습니다.')
            }

            // apiClient 대신 순수 axios(authAPI) 사용 → 인터셉터 루프 방지
            const { data } = await authAPI.refresh({
                refreshToken: authStore.refreshToken
            })

            // 새 Access Token, Refresh Token 저장
            authStore.setTokens(data.accessToken, data.refreshToken)

            // 대기 중이던 B, C 요청들을 새 토큰으로 일괄 처리
            flushQueue(null, data.accessToken)

            // 원래 실패했던 A 요청 재시도
            originalRequest.headers.Authorization = `Bearer ${data.accessToken}`
            return apiClient(originalRequest)

        } catch (refreshError) {
            // Refresh Token도 만료 → 모든 대기 요청 실패 처리
            flushQueue(refreshError, null)

            const { useAuthStore } = await import('@/stores/auth.js')
            useAuthStore().clearTokens()

            // 로그인 페이지가 아닐 때만 리다이렉트 (무한루프 방지)
            if (window.location.pathname !== '/auth/login') {
                window.location.href = '/auth/login'
            }

            return Promise.reject(refreshError)
        } finally {
            // 갱신 완료 (성공/실패 무관) → 플래그 해제
            isRefreshing = false
        }
    }
)