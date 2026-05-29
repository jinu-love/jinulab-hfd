import { apiClient, authAPI } from '@/api/auth.js'
import { getDeviceId, getDeviceName, getDeviceType } from '@/utils/device.js'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

/**
 * 인증 상태 전역 스토어 (Pinia)
 *
 * 토큰 저장 전략:
 *   - accessToken:  메모리(ref) 저장 → XSS 공격으로부터 보호
 *                   새로고침 시 소멸 → initializeAuth()로 복구
 *   - refreshToken: localStorage 저장 → 새로고침 후에도 유지
 *                   탈취 위험이 있으나 현재 구조에서 최선의 선택
 */
export const useAuthStore = defineStore('auth', () => {

  // ── 상태 ─────────────────────────────────────────────────────

  // Access Token: 메모리 저장 (페이지 새로고침 시 소멸)
  const accessToken = ref(null)

  // Refresh Token: localStorage 저장 (앱 재시작 후에도 유지)
  const refreshToken = ref(localStorage.getItem('refreshToken') || null)

  // ✅ 핵심: 초기화 완료 여부 플래그
  const initialized = ref(false)

  // ✅ 사용자 정보 상태 추가
  const userInfo = ref({
    accountId: null,
    email: null,
    name: null,
    company: null,
    department: null,
    division: null,
    team: null,
    part: null,
    rank: null,   // ✅ 직급
    position: null,   // ✅ 직책
    phone: null,
    profileImage: null,
    roles: [],
  })

  // ── Getters ──────────────────────────────────────────────────

  // 로그인 여부: accessToken이 존재하면 true
  const isAuthenticated = computed(() => !!accessToken.value)

  /**
   * JWT payload 디코딩
   * JWT 구조: header.payload.signature (Base64 인코딩)
   * payload에 sub(사용자명), authorities(권한), exp(만료시각) 등 포함
   */
  const decodedPayload = computed(() => {
    if (!accessToken.value) return null
    try {
      // JWT의 두 번째 부분(payload)을 Base64 디코딩
      return JSON.parse(atob(accessToken.value.split('.')[1]))
    } catch {
      return null
    }
  })

  // JWT sub 클레임 → 사용자 계정 ID
  const username = computed(() => decodedPayload.value?.sub || null)

  // JWT authorities 클레임에서 ROLE_ 접두사 제거
  // ex) "ROLE_ADMIN" → "ADMIN"
  const roles = computed(() =>
    (decodedPayload.value?.authorities || []).map(a => a.replace('ROLE_', ''))
  )

  // 관리자 여부
  const isAdmin = computed(() => roles.value.includes('ADMIN'))

  // ✅ 서버 응답 데이터로 사용자 정보 저장
  const setUserInfo = (data) => {
    userInfo.value = {
      accountId: data.accountId ?? null,
      email: data.email ?? null,
      name: data.name ?? null,
      company: data.company ?? null,
      department: data.department ?? null,
      division: data.division ?? null,
      team: data.team ?? null,
      part: data.part ?? null,
      rank: data.rank ?? null,   // ✅ 직급
      position: data.position ?? null,   // ✅ 직책
      phone: data.phone ?? null,
      profileImage: data.profileImage ?? null,
      roles: data.roles ?? [],
    }
  }

  // ✅ JWT payload에서 사용자 정보 파싱 (서버 응답에 user 없을 때 폴백)
  const setUserInfoFromToken = () => {
    const payload = decodedPayload.value
    if (!payload) return
    userInfo.value = {
      accountId: payload.sub ?? null,
      email: payload.email ?? null,
      name: payload.name ?? null,
      roles: (payload.authorities || []).map(a => a.replace('ROLE_', '')),
      profileImage: payload.profileImage ?? null,
    }
  }

  // ── Actions ──────────────────────────────────────────────────

  /**
   * 로그인
   * - 기기 정보(deviceId, deviceName, deviceType)를 함께 전송
   * - 서버는 기기별로 별도의 Refresh Token을 발급/관리
   * - 같은 기기로 재로그인 시 기존 토큰을 덮어씌워 중복 방지
   */
  const login = async (credentials) => {
    const { data } = await authAPI.login({
      ...credentials,
      deviceId: getDeviceId(),    // localStorage UUID (기기 고유 식별자)
      deviceName: getDeviceName(),  // "Chrome on Windows 11"
      deviceType: getDeviceType(),  // WEB / MOBILE / TABLET
    })
    setTokens(data.accessToken, data.refreshToken)
    initialized.value = true

    // 2. ✅ 사용자 정보 별도 조회
    await fetchUserInfo()
  }

  // ✅ 사용자 정보 조회 함수 (독립적으로 분리)
  const fetchUserInfo = async () => {
    try {
      const { data } = await apiClient.get('/users/me/profile', {
      });

      setUserInfo(data)

      userInfo.value.accountId;

    } catch (error) {
      // 토큰은 있지만 프로필 조회 실패 시 JWT에서 fallback
      setUserInfoFromToken()
    }
  }

  /**
   * 로그아웃
   * - 서버에 Refresh Token 삭제 요청 (DB에서 해당 기기 세션 제거)
   * - 서버 요청 실패해도 로컬 토큰은 반드시 초기화 (finally 보장)
   */
  const logout = async () => {
    try {
      if (refreshToken.value) {
        await authAPI.logout({ refreshToken: refreshToken.value })
      }
    } catch {
      // 서버 오류(네트워크 단절 등)여도 로컬 토큰 초기화 진행
    } finally {
      clearTokens()
    }
  }

  // ✅ 모든 탭/기기 전체 로그아웃
  const logoutAll = async () => {
    try {
      await authAPI.logoutAll()
    } catch {
      // 서버 실패해도 로컬 토큰 초기화
    } finally {
      clearTokens()
      // 다른 탭에게 로그아웃 알림
      localStorage.setItem('logout-event', Date.now().toString())
      localStorage.removeItem('logout-event')
    }
  }

  /**
   * 앱 시작 시 인증 상태 복구 (main.js에서 await로 호출)
   *
   * 흐름:
   *   1. localStorage에서 refreshToken 확인
   *   2. 서버 /api/auth/refresh 호출
   *   3. 새 accessToken + refreshToken 수신 → 저장
   *   4. 실패 시 clearTokens() → 로그인 필요 상태
   */
  // ✅ initialized = true를 finally에서 반드시 처리
  //2026-03-06 L.JW
  // const initializeAuth = async () => {
  //   if (!refreshToken.value) {
  //     initialized.value = true  // refreshToken 없어도 초기화 완료 표시
  //     return
  //   } // Refresh Token 없으면 로그인 필요
  //   try {
  //     const { data } = await authAPI.refresh({
  //       refreshToken: refreshToken.value
  //     })
  //     setTokens(data.accessToken, data.refreshToken)
  //   } catch {
  //     // Refresh Token 만료 또는 서버 오류 → 토큰 초기화
  //     clearTokens()
  //   } finally {
  //     initialized.value = true  // ✅ 성공/실패 무관하게 초기화 완료
  //   }
  // }

  const initializeAuth = async () => {
    // ✅ 가드 1: 이미 초기화 완료
    if (initialized.value) return

    // ✅ 가드 2: accessToken이 이미 메모리에 있으면 서버 호출 불필요
    if (accessToken.value) {
      initialized.value = true
      return
    }

    // ✅ 가드 3: refreshToken 없으면 로그인 필요
    if (!refreshToken.value) {
      initialized.value = true
      return
    }

    try {
      const { data } = await authAPI.refresh({ refreshToken: refreshToken.value })
      setTokens(data.accessToken, data.refreshToken)

      // ✅ 새로고침 후 복구 시에도 사용자 정보 조회
      await fetchUserInfo()

    } catch {
      clearTokens()
    } finally {
      initialized.value = true
    }
  }

  const refreshAccessToken = async () => {
    if (!refreshToken.value) throw new Error('RefreshToken 없음')
    const { data } = await authAPI.refresh({ refreshToken: refreshToken.value })
    setTokens(data.accessToken, data.refreshToken)

    // ✅ 토큰 갱신 시 사용자 정보도 갱신
    if (data.user) {
      setUserInfo(data.user)
    } else {
      setUserInfoFromToken()
    }
  }

  // ── 헬퍼 함수 (api/auth.js 인터셉터에서도 사용) ─────────────

  /**
   * 토큰 저장
   * - accessToken: Pinia 메모리
   * - refreshToken: Pinia 메모리 + localStorage
   */
  const setTokens = (access, refresh_) => {
    accessToken.value = access
    refreshToken.value = refresh_
    localStorage.setItem('refreshToken', refresh_)
  }

  /**
   * 토큰 초기화
   * - Pinia 상태 및 localStorage 모두 제거
   * - 로그아웃, 토큰 만료, 인증 실패 시 호출
   */
  const clearTokens = () => {
    accessToken.value = null
    refreshToken.value = null
    localStorage.removeItem('refreshToken')
    initialized.value = false

    // ✅ 로그아웃 시 사용자 정보 초기화
    userInfo.value = {
      accountId: null,
      email: null,
      name: null,
      roles: [],
      profileImage: null,
    }

    localStorage.removeItem('refreshToken')

  }

  // ✅ 전체 로그아웃 시 다른 탭도 로그아웃 처리
  window.addEventListener('storage', (event) => {
    if (event.key === 'logout-event' && event.newValue) {
      accessToken.value = null
      refreshToken.value = null
      initialized.value = false
      window.location.href = '/api/auth/login'
    }
  })

  return {
    // 상태
    accessToken,
    refreshToken,
    userInfo,
    // Getter
    isAuthenticated,
    username,
    roles,
    isAdmin,
    // Actions
    login,
    logout,
    logoutAll,
    initializeAuth,
    refreshAccessToken,
    // 헬퍼 (api/auth.js에서 직접 사용)
    setTokens,
    clearTokens,
    setUserInfo,     // ✅ 추가
    fetchUserInfo,   // ✅ 외부에서 직접 재조회 가능
  }
})