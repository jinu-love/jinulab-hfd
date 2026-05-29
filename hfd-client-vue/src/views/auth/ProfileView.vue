<script setup>
import { apiClient } from '@/api/auth.js';
import { useAuthStore } from '@/stores/auth';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';

const authStore = useAuthStore();
const toast = useToast();
const loading = ref(false);
const passwordLoading = ref(false);

const profile = ref({
    accountId: '',
    name: '',
    company: '',
    department: '',
    team: '',
    part: '',
    rank: '',
    position: '',
    phone: '',
    profileImage: ''
});

const passwordForm = ref({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
});

const passwordErrors = ref({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
});

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



onMounted(async () => {
    await fetchProfile();
});

const fetchProfile = async () => {
    try {
        const response = await apiClient.get('/users/me/profile');
        profile.value = response.data;
    } catch (error) {
        toast.add({ severity: 'error', summary: '오류', detail: '프로필 정보를 불러오지 못했습니다.', life: 3000 });
    }
};

const saveProfile = async () => {
    loading.value = true;
    try {
        await apiClient.put('/users/me/profile', profile.value);
        toast.add({ severity: 'success', summary: '저장 완료', detail: '프로필이 저장되었습니다.', life: 3000 });
    } catch (error) {
        toast.add({ severity: 'error', summary: '오류', detail: '프로필 저장에 실패했습니다.', life: 3000 });
    } finally {
        loading.value = false;
    }
};

const validatePassword = () => {
    passwordErrors.value = { currentPassword: '', newPassword: '', confirmPassword: '' };
    let valid = true;

    if (!passwordForm.value.currentPassword) {
        passwordErrors.value.currentPassword = '현재 비밀번호를 입력해주세요.';
        valid = false;
    }
    if (!passwordForm.value.newPassword) {
        passwordErrors.value.newPassword = '새 비밀번호를 입력해주세요.';
        valid = false;
    } else if (passwordForm.value.newPassword.length < 8) {
        passwordErrors.value.newPassword = '비밀번호는 8자 이상이어야 합니다.';
        valid = false;
    }
    if (!passwordForm.value.confirmPassword) {
        passwordErrors.value.confirmPassword = '비밀번호 확인을 입력해주세요.';
        valid = false;
    } else if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        passwordErrors.value.confirmPassword = '새 비밀번호와 일치하지 않습니다.';
        valid = false;
    }
    return valid;
};

const changePassword = async () => {
    if (!validatePassword()) return;
    passwordLoading.value = true;
    try {
        await apiClient.put('/users/me/password', passwordForm.value);
        toast.add({ severity: 'success', summary: '변경 완료', detail: '비밀번호가 변경되었습니다.', life: 3000 });
        passwordForm.value = { currentPassword: '', newPassword: '', confirmPassword: '' };
    } catch (error) {
        const msg = error.response?.data?.error || '비밀번호 변경에 실패했습니다.';
        toast.add({ severity: 'error', summary: '오류', detail: msg, life: 3000 });
    } finally {
        passwordLoading.value = false;
    }
};
</script>

<template>
    <Toast />
    <div class="card flex flex-col gap-4">
            <i class="pi pi-user text-primary" style="font-size: 1.5rem" />
            <div class="font-semibold text-xl">프로필 정보</div>
            <div class="flex flex-wrap gap-4">
                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">계정 아이디</label>
                    <InputText v-model="profile.accountId" disabled />
                </div>

                
                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">이름</label>
                    <InputText v-model="profile.name" placeholder="이름을 입력해주세요" />
                </div>

                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">회사</label>
                    <InputText v-model="profile.company" placeholder="회사를 입력해주세요" />
                </div>

                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">연락처</label>
                    <InputText v-model="profile.phone" placeholder="연락처를 입력해주세요" />
                </div>

                
            </div>

            <div class="flex flex-wrap gap-4">
                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">부서</label>
                    <Select v-model="profile.department" :options="deptValues" optionLabel="name" optionValue="code" placeholder="부서" class="w-full" />
                </div> 

                
                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">팀</label>
                    <InputText v-model="profile.team" placeholder="팀을 입력해주세요" />
                </div>

                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">파트</label>
                    <InputText v-model="profile.part" placeholder="파트를 입력해주세요" />
                </div>

                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">직급</label>
                    <Select v-model="profile.rank" :options="rankValues" optionLabel="name" optionValue="code" placeholder="직급" class="w-full" />
                </div>

                <div class="flex flex-col grow basis-0 gap-2">
                    <label class="font-medium mb-2 block">직책</label>
                    <InputText v-model="profile.position" placeholder="직책을 입력해주세요" />
                </div> 
            </div>
            <div class="flex justify-content-end mt-4" style="max-width: 700px;">
            <Button label="저장" icon="pi pi-save" :loading="loading" @click="saveProfile" />
            </div>

            
        </div>

    <!-- 비밀번호 변경 -->
        <div class="card flex flex-col gap-4">
            <div class="flex align-items-center gap-3 mb-5">
                    <i class="pi pi-lock text-primary" style="font-size: 1.5rem" />
                    <span class="text-xl font-semibold">비밀번호 변경</span>
                </div>

                <div class="grid formgrid p-fluid">

                    <div class="field col-12 md:col-4 mb-4">
                        <label class="font-medium mb-2 block">현재 비밀번호</label>
                        <Password v-model="passwordForm.currentPassword" :feedback="false" toggleMask
                            :invalid="!!passwordErrors.currentPassword" placeholder="현재 비밀번호를 입력해주세요" />
                        <small class="p-error">{{ passwordErrors.currentPassword }}</small>
                    </div>

                    <div class="field col-12 md:col-4 mb-4">
                        <label class="font-medium mb-2 block">새 비밀번호</label>
                        <Password v-model="passwordForm.newPassword" toggleMask
                            :invalid="!!passwordErrors.newPassword" placeholder="새 비밀번호를 입력해주세요" />
                        <small class="p-error">{{ passwordErrors.newPassword }}</small>
                    </div>

                    <div class="field col-12 md:col-4 mb-4">
                        <label class="font-medium mb-2 block">새 비밀번호 확인</label>
                        <Password v-model="passwordForm.confirmPassword" :feedback="false" toggleMask
                            :invalid="!!passwordErrors.confirmPassword" placeholder="새 비밀번호를 다시 입력해주세요" />
                        <small class="p-error">{{ passwordErrors.confirmPassword }}</small>
                    </div>

                </div>

                <div class="flex justify-content-end mt-3">
                    <Button label="비밀번호 변경" icon="pi pi-lock" :loading="passwordLoading" @click="changePassword" />
                </div>
        </div>
</template>