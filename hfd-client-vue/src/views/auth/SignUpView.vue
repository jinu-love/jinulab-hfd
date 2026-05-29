<template>
    <FloatingConfigurator />
    <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
        <div class="flex flex-col items-center justify-center">
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%); width: 480px; max-width: 95vw;">
                <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px; overflow: hidden; word-break: break-word;">
                    <div class="text-center mb-8">
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">Welcome To Happy Friday</div>
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">회원가입</div>
                    </div>

                    <!-- 2단계 스텝 진행바 -->
                    <div class="mb-6">
                        <!--
                        <div class="flex justify-content-between text-sm text-500 mb-2">
                            <span :class="['font-medium', step === 1 ? 'text-primary' : 'text-400']"> <i class="pi pi-envelope mr-1" />1단계: 이메일 인증 </span>
                            <span :class="['font-medium', step === 2 ? 'text-primary' : 'text-400']"> <i class="pi pi-lock ml-1" />2단계: 계정 설정 </span>
                        </div>
                        -->
                        <div class="flex text-sm text-500 mb-2">
                            <span :class="['font-medium', step === 1 ? 'text-primary' : 'text-400']" style="width: 50%">
                                <i class="pi pi-envelope mr-1" />1단계: 이메일 인증
                            </span>
                            <span :class="['font-medium', step === 2 ? 'text-primary' : 'text-400']" style="width: 50%">
                                <i class="pi pi-lock ml-1" />2단계: 계정 설정
                            </span>
                        </div>

                        <div class="w-full border-round-xl overflow-hidden" style="height: 6px; background: var(--surface-200)">
                            <div class="h-full bg-primary border-round-xl transition-all transition-duration-500" :style="{ width: step === 1 ? '50%' : '100%' }" />
                        </div>
                    </div>

                    <!-- 에러/성공 전역 메시지 -->
                    <Message v-if="globalError" severity="error" class="mb-4 w-full" :closable="true" @close="globalError = ''">
                        {{ globalError }}
                    </Message>
                    <Message v-if="globalSuccess" severity="success" class="mb-4 w-full" :closable="false">
                        {{ globalSuccess }}
                    </Message>

                    <!-- ────────────── STEP 1: 이메일 인증 ────────────── -->
                    <form v-if="step === 1" @submit.prevent="handleSendCode" class="register-form">
                        <!-- 이메일 입력 -->
                        <div class="field mb-5">
                            <label for="email" class="font-medium text-500 text-sm mb-2 block"> 이메일 (계정 ID) </label>

                            <InputGroup>
                                <InputGroupAddon>
                                    <i class="pi pi-envelope"></i>
                                </InputGroupAddon>
                                <InputText
                                    id="email"
                                    v-model="form.email"
                                    type="email"
                                    placeholder="example@email.com"
                                    class="w-full"
                                    :invalid="!!fieldErrors.email"
                                    :disabled="codeSent || loading"
                                    autocomplete="email"
                                    @input="fieldErrors.email = ''"
                                />
                            </InputGroup>

                            <small v-if="fieldErrors.email" class="p-error block mt-1">
                                {{ fieldErrors.email }}
                            </small>
                        </div>

                        <!-- 인증 코드 입력 (발송 후 표시) -->
                        <Transition name="slide-down">
                            <div v-if="codeSent" class="field mb-5">
                                <label class="font-medium text-500 text-sm mb-2 flex justify-content-between">
                                    <span><i class="pi pi-key mr-1" />인증 코드</span>
                                    <span :class="['font-bold text-sm', remainingTime <= 60 ? 'text-red-500' : 'text-primary']"> <i class="pi pi-clock mr-1" />{{ formattedTime }} </span>
                                </label>
                                <div class="p-inputgroup">
                                    <span class="p-inputgroup-addon">
                                        <i class="pi pi-shield text-500" />
                                    </span>
                                    <InputText
                                        v-model="form.verificationCode"
                                        placeholder="6자리 코드 입력"
                                        class="w-full"
                                        maxlength="6"
                                        inputmode="numeric"
                                        :invalid="!!fieldErrors.verificationCode"
                                        :disabled="loading"
                                        @input="fieldErrors.verificationCode = ''"
                                    />
                                </div>
                                <small v-if="fieldErrors.verificationCode" class="p-error block mt-1">
                                    {{ fieldErrors.verificationCode }}
                                </small>
                                <div class="flex align-items-center justify-content-end mt-2">
                                    <Button label="재발송" icon="pi pi-refresh" link size="small" class="p-0 text-sm" :loading="loading" :disabled="loading" type="button" @click="resendCode" />
                                </div>
                            </div>
                        </Transition>

                        <!-- Step 1 버튼 -->
                        <Button v-if="!codeSent" type="submit" label="인증 코드 발송" icon="pi pi-send" class="w-full mb-3" :loading="loading" :disabled="!form.email || loading" size="large" />
                        <Button v-else type="button" label="다음 단계" icon="pi pi-arrow-right" iconPos="right" class="w-full mb-3" :loading="loading" :disabled="form.verificationCode.length !== 6 || loading" size="large" @click="goToStep2" />

                        <!-- 로그인 이동 -->
                        <Divider align="center">
                            <span class="text-400 text-sm px-2">또는</span>
                        </Divider>
                        <div class="text-center mt-3">
                            <span class="text-500 text-sm">이미 계정이 있으신가요?</span>
                            <router-link to="/auth/login" class="text-primary font-medium text-sm ml-2 no-underline hover:underline">로그인 </router-link>
                        </div>
                    </form>

                    <!-- ────────────── STEP 2: 계정 설정 ────────────── -->
                    <form v-else @submit.prevent="handleRegister" class="register-form">
                        <!-- 이메일 인증 완료 뱃지 -->
                        <div class="flex align-items-center gap-2 mb-5 p-3 border-round-lg surface-100">
                            <i class="pi pi-check-circle text-green-500 text-xl" />
                            <div>
                                <div class="text-sm font-medium text-700">이메일 인증 완료</div>
                                <div class="text-xs text-500">{{ form.email }}</div>
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
                                    placeholder="8자 이상, 영문+숫자+특수문자"
                                    :feedback="false"
                                    toggleMask
                                    class="w-full"
                                    inputClass="w-full"
                                    :invalid="!!fieldErrors.password"
                                    :disabled="loading"
                                    autocomplete="new-password"
                                    @input="fieldErrors.password = ''"
                                />
                            </InputGroup>

                            <small v-if="fieldErrors.password" class="p-error block mt-1">
                                {{ fieldErrors.password }}
                            </small>

                            <!-- 비밀번호 강도 바 -->
                            <div v-if="form.password" class="mt-2">
                                <div class="flex gap-1 mb-1">
                                    <div
                                        v-for="i in 4"
                                        :key="i"
                                        class="flex-1 border-round-sm transition-all transition-duration-300"
                                        style="height: 4px"
                                        :style="{
                                            background: i <= pwStrength ? pwStrengthColor : 'var(--surface-200)'
                                        }"
                                    />
                                </div>
                                <small :style="{ color: pwStrengthColor }" class="font-medium text-xs">
                                    {{ pwStrengthLabel }}
                                </small>
                            </div>
                        </div>

                        <!-- 비밀번호 확인 -->
                        <div class="field mb-6">
                            <label for="confirm-password" class="font-medium text-500 text-sm mb-2 block"> 비밀번호 확인 </label>
                            <InputGroup>
                                <InputGroupAddon>
                                    <i class="pi pi-lock"></i>
                                </InputGroupAddon>
                                <Password
                                    id="confirm-password"
                                    v-model="form.confirmPassword"
                                    placeholder="비밀번호를 다시 입력하세요"
                                    :feedback="false"
                                    toggleMask
                                    class="w-full"
                                    inputClass="w-full"
                                    :invalid="!!fieldErrors.confirmPassword"
                                    :disabled="loading"
                                    autocomplete="new-password"
                                    @input="fieldErrors.confirmPassword = ''"
                                />
                            </InputGroup>
                            <small v-if="fieldErrors.confirmPassword" class="p-error block mt-1">
                                {{ fieldErrors.confirmPassword }}
                            </small>
                            <!-- 일치 확인 인디케이터 -->
                            <small v-if="form.confirmPassword && !fieldErrors.confirmPassword" :class="['block mt-1 font-medium text-xs', passwordMatch ? 'text-green-500' : 'text-red-500']">
                                <i :class="['mr-1', passwordMatch ? 'pi pi-check' : 'pi pi-times']" />
                                {{ passwordMatch ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다' }}
                            </small>
                        </div>

                        <label for="user-info" class="font-medium text-500 text-sm mb-2 block"> 사용자 정보 </label>
                        <InputGroup class="w-full mb-2">
                            <InputGroupAddon>
                                <i class="pi pi-user"></i>
                            </InputGroupAddon>
                            <InputText v-model="form.name" placeholder="이름" />
                        </InputGroup>

                        <InputGroup class="w-full mb-2">
                            <InputGroupAddon>
                                <i class="pi pi-phone"></i>
                            </InputGroupAddon>
                            <InputText v-model="form.phone" placeholder="전화번호 하이픈(-)없이 입력" />
                        </InputGroup>

                        <div class="flex flex-col md:flex-row gap-2 mb-2">
                            <Select v-model="form.dept" :options="deptValues" optionLabel="name" placeholder="부서" class="w-full" />
                            <Select v-model="form.rank" :options="rankValues" optionLabel="name" placeholder="직급" class="w-full" />
                        </div>

                        <!-- Step 2 버튼들 -->
                        <Button type="submit" label="회원가입 완료" icon="pi pi-user-plus" class="w-full mb-3" :loading="loading" :disabled="!canSubmit" size="large" />
                        <Button type="button" label="이전 단계로" icon="pi pi-arrow-left" severity="secondary" outlined class="w-full" :disabled="loading" size="large" @click="goBackStep1" />
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { authAPI } from '@/api/auth.js';
import { computed, onUnmounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

import Button from 'primevue/button';
import Divider from 'primevue/divider';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import Password from 'primevue/password';

const router = useRouter();

// ── 상태 ────────────────────────────────────────────────────────────────
const step = ref(1);
const loading = ref(false);
const codeSent = ref(false);
const remainingTime = ref(300);
const globalError = ref('');
const globalSuccess = ref('');
let countdownTimer = null;

const form = reactive({
    email: '',
    verificationCode: '',
    password: '',
    confirmPassword: '',
    name: '',
    phone: '',
    dept: '',
    rank: ''
});

const fieldErrors = reactive({
    email: '',
    verificationCode: '',
    password: '',
    confirmPassword: ''
});

// ── 타이머 ──────────────────────────────────────────────────────────────
const formattedTime = computed(() => {
    const m = Math.floor(remainingTime.value / 60)
        .toString()
        .padStart(2, '0');
    const s = (remainingTime.value % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
});

function startCountdown() {
    clearInterval(countdownTimer);
    remainingTime.value = 300;
    countdownTimer = setInterval(() => {
        remainingTime.value--;
        if (remainingTime.value <= 0) {
            clearInterval(countdownTimer);
            codeSent.value = false;
            form.verificationCode = '';
            globalError.value = '인증 코드가 만료되었습니다. 다시 발송해주세요.';
            globalSuccess.value = '';
        }
    }, 1000);
}

onUnmounted(() => clearInterval(countdownTimer));

// ── 비밀번호 강도 ────────────────────────────────────────────────────────
const pwStrength = computed(() => {
    const pw = form.password;
    if (!pw) return 0;
    let score = 0;
    if (pw.length >= 8) score++;
    if (/[A-Z]/.test(pw)) score++;
    if (/[0-9]/.test(pw)) score++;
    if (/[^A-Za-z0-9]/.test(pw)) score++;
    return score;
});

const pwStrengthColor = computed(() => {
    if (pwStrength.value <= 1) return '#ef4444';
    if (pwStrength.value === 2) return '#f97316';
    if (pwStrength.value === 3) return '#eab308';
    return '#22c55e';
});

const pwStrengthLabel = computed(() => {
    const labels = ['', '매우 약함', '약함', '보통', '강함'];
    return labels[pwStrength.value] || '';
});

const passwordMatch = computed(() => form.password === form.confirmPassword && form.confirmPassword.length > 0);

const canSubmit = computed(() => form.password.length >= 8 && pwStrength.value >= 2 && passwordMatch.value && !loading.value);

// ── 유효성 검사 ──────────────────────────────────────────────────────────
function validateEmail() {
    if (!form.email.trim()) {
        fieldErrors.email = '이메일을 입력해주세요.';
        return false;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
        fieldErrors.email = '올바른 이메일 형식을 입력해주세요.';
        return false;
    }
    return true;
}

function validateCode() {
    if (form.verificationCode.length !== 6) {
        fieldErrors.verificationCode = '6자리 인증 코드를 입력해주세요.';
        return false;
    }
    return true;
}

function validatePassword() {
    let valid = true;
    fieldErrors.password = '';
    fieldErrors.confirmPassword = '';

    if (form.password.length < 8) {
        fieldErrors.password = '비밀번호는 8자 이상이어야 합니다.';
        valid = false;
    } else if (pwStrength.value < 2) {
        fieldErrors.password = '더 강한 비밀번호를 사용해주세요. (영문 대소문자, 숫자, 특수문자 조합)';
        valid = false;
    }

    if (!form.confirmPassword) {
        fieldErrors.confirmPassword = '비밀번호 확인을 입력해주세요.';
        valid = false;
    } else if (!passwordMatch.value) {
        fieldErrors.confirmPassword = '비밀번호가 일치하지 않습니다.';
        valid = false;
    }

    return valid;
}

// ── 인증 코드 발송 ───────────────────────────────────────────────────────
async function handleSendCode() {
    if (!validateEmail()) return;

    globalError.value = '';
    globalSuccess.value = '';
    loading.value = true;

    try {
        await authAPI.sendCode({ email: form.email });
        codeSent.value = true;
        startCountdown();
        globalSuccess.value = `${form.email}으로 인증 코드가 발송되었습니다. 5분 내에 입력해주세요.`;
    } catch (err) {
        const msg = err.response?.data?.error;
        if (err.response?.status === 400) {
            fieldErrors.email = msg || '이미 사용 중인 이메일입니다.';
        } else {
            globalError.value = msg || '이메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.';
        }
    } finally {
        loading.value = false;
    }
}

// ── 재발송 ──────────────────────────────────────────────────────────────
async function resendCode() {
    if (!validateEmail()) return;
    codeSent.value = false;
    form.verificationCode = '';
    globalSuccess.value = '';
    globalError.value = '';
    await handleSendCode();
}

// ── Step 2로 이동 (인증 코드 확인) ──────────────────────────────────────
async function goToStep2() {
    if (!validateCode()) return;
    globalError.value = '';
    globalSuccess.value = '';
    loading.value = true;

    try {
        // ✅ 서버에 인증 코드 검증 요청
        await authAPI.verifyCode({
            email: form.email,
            code: form.verificationCode
        });

        // 검증 성공 → 2단계로 이동
        clearInterval(countdownTimer);
        step.value = 2;
    } catch (err) {
        
        // const msg = err.response?.data?.error || err.response?.data;
        // const status = err.response?.status;

        const data = err.response?.data;
        const status = err.response?.status;
        const msg = typeof data === 'string' 
            ? data 
            : (data?.description || data?.code || data?.error || '');

        if (status === 400) {
            if (msg?.includes('만료')) {
                // 만료된 경우 → 코드 입력 초기화 + 재발송 유도
                codeSent.value = false;
                form.verificationCode = '';
                globalError.value = '인증 코드가 만료되었습니다. 다시 발송해주세요.';
            } else {
                // 코드 불일치
                fieldErrors.verificationCode = '인증 코드가 올바르지 않습니다. 다시 확인해주세요.';
            }
        } else {
            globalError.value = '인증 확인 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
        }
    } finally {
        loading.value = false;
    }
}

// ── Step 1로 되돌아가기 ─────────────────────────────────────────────────
function goBackStep1() {
    step.value = 1;
    form.password = '';
    form.confirmPassword = '';
    fieldErrors.password = '';
    fieldErrors.confirmPassword = '';
    globalError.value = '';
    globalSuccess.value = '';
}

// ── 회원가입 최종 제출 ───────────────────────────────────────────────────
async function handleRegister() {
    if (!validatePassword()) return;

    globalError.value = '';
    loading.value = true;

    try {
        // accountId = 이메일 (dev.jinus.iam 규격)
        await authAPI.register({
            accountId: form.email,
            email: form.email,
            password: form.password,
            verificationCode: form.verificationCode,
            name: form.name,
            phone: form.phone,
            department: form.dept.name,
            rank: form.rank.name,
            company: '아이셋디엑스'
        });

        clearInterval(countdownTimer);
        // 로그인 페이지로 이동 + 완료 쿼리
        router.push('/auth/login?registered=1');
    } catch (err) {
        const msg = err.response?.data?.error;
        if (err.response?.status === 401) {
            // 인증 코드 문제 → Step 1로 되돌림
            step.value = 1;
            codeSent.value = false;
            form.verificationCode = '';
            globalError.value = msg || '인증 코드가 올바르지 않거나 만료되었습니다. 다시 발송해주세요.';
        } else if (err.response?.status === 400) {
            globalError.value = msg || '이미 사용 중인 이메일입니다.';
        } else {
            globalError.value = msg || '회원가입 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
        }
    } finally {
        loading.value = false;
    }
}

const deptValues = ref([
    { name: 'ITO 사업부', code: 'ITO 사업부' },
    { name: 'SI 사업부', code: 'SI 사업부' }
]);

const rankValues = ref([
    { name: '사원', code: '사원' },
    { name: '대리', code: '대리' },
    { name: '과장', code: '과장' },
    { name: '차장', code: '차장' },
    { name: '부장', code: '부장' },
    { name: '이사', code: '이사' },
    { name: '상무', code: '상무' },
    { name: '전무', code: '전무' }
]);
</script>
