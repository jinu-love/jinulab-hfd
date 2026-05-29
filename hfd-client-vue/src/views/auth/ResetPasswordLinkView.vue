<template>
    <FloatingConfigurator />
    <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
        <div class="flex flex-col items-center justify-center">
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%); width: 480px; max-width: 95vw;">
                <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px; overflow: hidden; word-break: break-word;">
                    <!-- 타이틀 -->
                    <div class="text-center mb-8">
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">Happy Friday</div>
                        <div class="text-surface-900 dark:text-surface-0 text-2xl font-medium mb-1">새 비밀번호 설정</div>
                        <span class="text-surface-500 text-sm"> 이메일로 전송된 링크를 통해 비밀번호를 재설정합니다. </span>
                    </div>

                    <!-- 메시지 -->
                    <Message v-if="globalError" severity="error" class="mb-4 w-full" :closable="true" @close="globalError = ''">
                        {{ globalError }}
                    </Message>
                    <Message v-if="globalSuccess" severity="success" class="mb-4 w-full" :closable="false">
                        {{ globalSuccess }}
                    </Message>

                    <!-- 토큰이 없으면 안내 -->
                    <div v-if="!resetToken" class="text-center">
                        <p class="text-surface-500 text-sm mb-4">유효하지 않은 접근입니다. 이메일에 있는 링크를 다시 확인해주세요.</p>
                        <Button label="로그인 화면으로" icon="pi pi-sign-in" class="w-full" @click="goLogin" />
                    </div>

                    <!-- 토큰이 있을 때만 폼 표시 -->
                    <form v-else @submit.prevent="handleResetPassword" class="reset-form mt-4">
                        <!-- 새 비밀번호 -->
                        <div class="field mb-4">
                            <label for="password" class="font-medium text-500 text-sm mb-2 block">새 비밀번호</label>
                            <InputGroup>
                                <InputGroupAddon>
                                    <i class="pi pi-lock" />
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

                            <!-- 비밀번호 강도 -->
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

                        <!-- 새 비밀번호 확인 -->
                        <div class="field mb-6">
                            <label for="confirm-password" class="font-medium text-500 text-sm mb-2 block"> 새 비밀번호 확인 </label>
                            <InputGroup>
                                <InputGroupAddon>
                                    <i class="pi pi-lock" />
                                </InputGroupAddon>
                                <Password
                                    id="confirm-password"
                                    v-model="form.confirmPassword"
                                    placeholder="새 비밀번호를 다시 입력하세요"
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
                            <small v-if="form.confirmPassword && !fieldErrors.confirmPassword" :class="['block mt-1 font-medium text-xs', passwordMatch ? 'text-green-500' : 'text-red-500']">
                                <i :class="['mr-1', passwordMatch ? 'pi pi-check' : 'pi pi-times']" />
                                {{ passwordMatch ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다' }}
                            </small>
                        </div>

                        <Button type="submit" label="비밀번호 재설정 완료" icon="pi pi-key" class="w-full mb-3" :loading="loading" :disabled="!canSubmit" size="large" />
                        <Button type="button" label="로그인 화면으로" icon="pi pi-sign-in" severity="secondary" outlined class="w-full" :disabled="loading" size="large" @click="goLogin" />
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { authAPI } from '@/api/auth.js';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Message from 'primevue/message';
import Password from 'primevue/password';
import { computed, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();

const resetToken = computed(() => route.query.token || null);

const loading = ref(false);
const globalError = ref('');
const globalSuccess = ref('');

const form = reactive({
    password: '',
    confirmPassword: ''
});

const fieldErrors = reactive({
    password: '',
    confirmPassword: ''
});

// 비밀번호 강도
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

function goLogin() {
    router.push('/auth/login');
}

// POST /api/auth/reset-password
// ResetPasswordRequest: { token, newPassword }
async function handleResetPassword() {
    if (!resetToken.value) {
        globalError.value = '유효하지 않은 링크입니다.';
        return;
    }
    if (!validatePassword()) return;

    loading.value = true;
    globalError.value = '';
    globalSuccess.value = '';

    try {
        await authAPI.resetPassword({
            token: resetToken.value,
            newPassword: form.password
        });

        globalSuccess.value = '비밀번호가 재설정되었습니다. 새 비밀번호로 로그인해주세요.';
        setTimeout(() => {
            router.push('/auth/login?reset=1');
        }, 1500);
    } catch (err) {
        const msg = err.response?.data?.error;
        const status = err.response?.status;

        if (status === 400 || status === 401) {
            globalError.value = msg || '링크가 유효하지 않거나 만료되었습니다. 다시 비밀번호 찾기를 진행해주세요.';
        } else {
            globalError.value = msg || '비밀번호 재설정 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
        }
    } finally {
        loading.value = false;
    }
}
</script>
