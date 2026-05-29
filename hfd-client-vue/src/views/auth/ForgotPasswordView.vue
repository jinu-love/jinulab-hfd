<template>
    <FloatingConfigurator />
    <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
        <div class="flex flex-col items-center justify-center">
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%); width: 480px; max-width: 95vw;">
                <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px; overflow: hidden; word-break: break-word;">
                    <!-- 타이틀 -->
                    <div class="text-center mb-8">
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">Happy Friday</div>
                        <div class="text-surface-900 dark:text-surface-0 text-2xl font-medium mb-1">비밀번호 찾기</div>
                        <span class="text-surface-500 text-sm"> 가입하신 이메일로 비밀번호 재설정 링크를 보내 드립니다. </span>
                    </div>

                    <!-- 메시지 -->
                    <Message v-if="globalError" severity="error" class="mb-4 w-full" :closable="true" @close="globalError = ''">
                        {{ globalError }}
                    </Message>
                    <Message v-if="globalSuccess" severity="success" class="mb-4 w-full" :closable="false">
                        <span style="white-space: pre-line">{{ globalSuccess }}</span>
                    </Message>

                    <!-- 폼 -->
                    <form @submit.prevent="handleSubmit" class="forgot-form">
                        <!-- 이메일 -->
                        <div class="field mb-5">
                            <label for="email" class="font-medium text-500 text-sm mb-2 block"> 이메일 (계정 ID) </label>
                            <InputGroup>
                                <InputGroupAddon>
                                    <i class="pi pi-envelope" />
                                </InputGroupAddon>
                                <InputText id="email" v-model="email" type="email" placeholder="example@email.com" class="w-full" :invalid="!!emailError" :disabled="loading" autocomplete="email" @input="emailError = ''" />
                            </InputGroup>
                            <small v-if="emailError" class="p-error block mt-1">
                                {{ emailError }}
                            </small>
                        </div>

                        <Button type="submit" label="비밀번호 재설정 링크 보내기" icon="pi pi-send" class="w-full mb-3" :loading="loading" :disabled="!email || loading" size="large" />

                        <Divider align="center">
                            <span class="text-400 text-sm px-2">또는</span>
                        </Divider>
                        <div class="text-center mt-3">
                            <span class="text-500 text-sm">비밀번호가 기억나셨나요?</span>
                            <router-link to="/auth/login" class="text-primary font-medium text-sm ml-2 no-underline hover:underline"> 로그인 </router-link>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { authAPI } from '@/api/auth.js';
import Button from 'primevue/button';
import Divider from 'primevue/divider';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputText from 'primevue/inputtext';
import Message from 'primevue/message';
import { ref } from 'vue';

const email = ref('');
const emailError = ref('');
const loading = ref(false);
const globalError = ref('');
const globalSuccess = ref('');

function validateEmail() {
    if (!email.value.trim()) {
        emailError.value = '이메일을 입력해주세요.';
        return false;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
        emailError.value = '올바른 이메일 형식을 입력해주세요.';
        return false;
    }
    return true;
}

async function handleSubmit() {
    if (!validateEmail()) return;

    loading.value = true;
    globalError.value = '';
    globalSuccess.value = '';

    try {
        // ForgotPasswordRequest: { email }
        await authAPI.forgotPassword({ email: email.value });
        globalSuccess.value = `${email.value} 로 \n비밀번호 재설정 링크가 전송되었습니다.`;
    } catch (err) {
        const msg = err.response?.data?.error;
        if (err.response?.status === 400) {
            emailError.value = msg || '등록된 이메일이 아닙니다.';
        } else {
            globalError.value = msg || '요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
        }
    } finally {
        loading.value = false;
    }
}
</script>
