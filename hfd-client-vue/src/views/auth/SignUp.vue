<script setup>
import FloatingConfigurator from '@/components/FloatingConfigurator.vue';
import { useAuthStore } from '@/stores/auth';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const authStore = useAuthStore();
const signUpForm = reactive({ email: '', password: '', name: '', dept: '', rank: '', phone: '' });

const handleSignUp = async () => {
    const result = await authStore.signUp({
        email: signUpForm.email,
        password: signUpForm.password,
        name: signUpForm.name,
        phone: signUpForm.phone,
        dept: signUpForm.dept.name,
        rank: signUpForm.rank.name
    });
    if (result.success) {
        alert('회원가입에 성공하였습니다. 로그인해주세요!');
        await router.push('/');
    } else {
        alert(result.error);
    }
};

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

<template>
    <FloatingConfigurator />
    <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
        <div class="flex flex-col items-center justify-center">
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%)">
                <form @submit.prevent="handleSignUp">
                    <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px">
                        <div class="text-center mb-8">
                            <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">Welcome To Happy Friday</div>
                            <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">회원가입</div>
                        </div>
                        <div class="text-center md:w-[30rem]">
                            <InputGroup class="w-full mb-4">
                                <InputGroupAddon>
                                    <i class="pi pi-envelope"></i>
                                </InputGroupAddon>
                                <InputText v-model="signUpForm.email" placeholder="이메일" />
                            </InputGroup>
                            <InputGroup class="w-full mb-4">
                                <InputGroupAddon>
                                    <i class="pi pi-lock"></i>
                                </InputGroupAddon>
                                <Password id="password1" v-model="signUpForm.password" placeholder="비밀번호" :toggleMask="true" class="mb-4" fluid :feedback="false"></Password>
                            </InputGroup>

                            <InputGroup class="w-full mb-4">
                                <InputGroupAddon>
                                    <i class="pi pi-user"></i>
                                </InputGroupAddon>
                                <InputText v-model="signUpForm.name" placeholder="이름" />
                            </InputGroup>

                            <InputGroup class="w-full mb-4">
                                <InputGroupAddon>
                                    <i class="pi pi-phone"></i>
                                </InputGroupAddon>
                                <InputText v-model="signUpForm.phone" placeholder="하이픈(-)없이 입력" />
                            </InputGroup>

                            <div class="flex flex-col md:flex-row gap-4 mb-4">
                                <Select v-model="signUpForm.dept" :options="deptValues" optionLabel="name" placeholder="부서" class="w-full" />
                                <Select v-model="signUpForm.rank" :options="rankValues" optionLabel="name" placeholder="직급" class="w-full" />
                            </div>

                            <Button type="submit" icon="pi pi-user-plus" label="회원가입" class="w-full"></Button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>
