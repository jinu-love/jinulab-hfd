/**
 * 기기 정보 유틸리티
 *
 * 역할:
 *   - 브라우저별 고유 deviceId 생성 및 관리 (localStorage 저장)
 *   - User-Agent 파싱으로 기기명, 기기 타입 추출
 *   - 서버에 기기 정보를 전달해 기기별 Refresh Token 관리에 활용
 */



/**
 * 현재 브라우저/기기의 고유 식별자 반환
 * - localStorage에 저장 → 같은 브라우저면 항상 동일한 ID 반환
 * - localStorage 초기화 시 새 ID 생성 (다른 기기로 인식)
 */
export const getDeviceId = () => {
    let deviceId = localStorage.getItem('deviceId')
    if (!deviceId) {
        deviceId = generateUUID()
        localStorage.setItem('deviceId', deviceId)
    }
    return deviceId
}

/**
 * User-Agent 파싱으로 사람이 읽기 쉬운 기기명 반환
 * ex) "Chrome on Windows 10/11", "Safari on iOS"
 */
export const getDeviceName = () => {
    const ua = navigator.userAgent
    const browser = detectBrowser(ua)
    const os = detectOS(ua)
    return `${browser} on ${os}`
}

/**
 * User-Agent 기반 기기 타입 분류
 * - MOBILE: 스마트폰 (iPhone, Android Mobile 등)
 * - TABLET: 태블릿 (iPad, Android Tablet 등)
 * - WEB:    데스크탑/노트북
 */
export const getDeviceType = () => {
    const ua = navigator.userAgent
    if (/iPhone|Android.*Mobile|Windows Phone/.test(ua)) return 'MOBILE'
    if (/iPad|Android(?!.*Mobile)|Tablet/.test(ua)) return 'TABLET'
    return 'WEB'
}

/**
 * 기기 타입에 따른 이모지 아이콘 반환
 * SessionView.vue에서 세션 목록 UI에 활용
 */
export const getDeviceIcon = (deviceType) => {
    const icons = { WEB: '🖥️', MOBILE: '📱', TABLET: '📟' }
    return icons[deviceType] || '💻'
}

// ── 내부 헬퍼 함수 ────────────────────────────────────────────

const detectBrowser = (ua) => {
    if (/Edg\/|Edge\//.test(ua)) return 'Edge'
    if (/OPR\/|Opera/.test(ua)) return 'Opera'
    if (/Chrome/.test(ua) && !/Chromium/.test(ua)) return 'Chrome'
    if (/Chromium/.test(ua)) return 'Chromium'
    if (/Firefox/.test(ua)) return 'Firefox'
    if (/Safari/.test(ua) && !/Chrome/.test(ua)) return 'Safari'
    return 'Unknown Browser'
}

const detectOS = (ua) => {
    if (/iPhone/.test(ua)) return 'iOS'
    if (/iPad/.test(ua)) return 'iPadOS'
    if (/Android/.test(ua)) return 'Android'
    if (/Windows NT 10/.test(ua)) return 'Windows 10/11'
    if (/Windows/.test(ua)) return 'Windows'
    if (/Mac OS X/.test(ua)) return 'macOS'
    if (/Linux/.test(ua)) return 'Linux'
    return 'Unknown OS'
}

/**
 * 외부 패키지 없이 UUID v4 생성
 * crypto.randomUUID()가 없는 환경(HTTP)도 대응
 */
// const generateUUID = () => {
//   // crypto.randomUUID 지원 시 우선 사용 (더 안전)
//   if (typeof crypto !== 'undefined' && crypto.randomUUID) {
//     return crypto.randomUUID()
//   }
//   // 폴백: Math.random 기반 UUID 생성
//   return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
//     const r = (Math.random() * 16) | 0
//     return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16)
//   })
// }

/**
 * UUID v4 생성 (3단계 폴백)
 *
 * 1순위: crypto.randomUUID()    → HTTPS/localhost 최신 브라우저
 * 2순위: crypto.getRandomValues() → HTTP 환경 또는 구버전 브라우저
 * 3순위: Math.random()           → crypto 자체가 없는 최구버전 환경
 */
const generateUUID = () => {
    // 1순위: crypto.randomUUID (HTTPS 환경 전용)
    if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
        return crypto.randomUUID()
    }

    // 2순위: crypto.getRandomValues (HTTP 환경에서도 동작)
    if (typeof crypto !== 'undefined' && typeof crypto.getRandomValues === 'function') {
        return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, (c) =>
            (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
        )
    }

    // 3순위: Math.random 폴백 (crypto 자체가 없는 환경)
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
        const r = (Math.random() * 16) | 0
        return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16)
    })
}