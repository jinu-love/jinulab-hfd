<script setup>
import { apiClient } from '@/api/auth.js';
import { onMounted, ref, watch } from 'vue';

const customers = ref();
const selectedCustomers = ref();
const filters = ref();
const name = ref();
const userInfo = JSON.parse(localStorage.getItem('user-info'));

const radioValue = ref(null);
const isOvertime = ref(false);
onMounted(() => {
    radioValue.value = '초과근무';
});

watch(radioValue, (currentValue, prevValue) => {
    if (radioValue.value === '초과근무') {
        isOvertime.value = true;
        console.info(radioValue);
    } else {
        isOvertime.value = false;
        console.info(radioValue);
    }
});
const overtimeStatusList = ref(null);
const dayOffStatusList = ref(null);
const getOvertimeStatus = async () => {
    try {
        const response = await apiClient.get('v1/get/select/overtime/status', {
            params: {
                name: name.value,
                radioValue: radioValue.value
            }
        });
        if (isOvertime.value == true) {
            overtimeStatusList.value = response.data.overtimeStatusList;
        } else {
            dayOffStatusList.value = response.data.overtimeStatusList;
        }
    } catch (error) {

        // 인터셉터가 refresh 실패 후 리다이렉트한 경우 response가 없음 → 무시
        if (!error.response) return;
        
        console.error('getOvertimeStatus error', error);
        alert(error.response.data.code + ': ' + error.response.data.description);
    }
};

const size = ref({ label: 'Normal', value: 'null' });
const sizeOptions = ref([
    { label: 'Small', value: 'small' },
    { label: 'Normal', value: 'null' },
    { label: 'Large', value: 'large' }
]);
</script>

<template>
    <div class="card flex flex-col gap-4">
        <div class="font-semibold text-xl">조건</div>
        <div class="flex flex-col md:flex-row gap-4">
            <FloatLabel variant="on">
                <InputText id="name" v-model="name" :invalid="!name" />
                <label for="name">이름</label>
            </FloatLabel>
            <div class="flex items-center">
                <RadioButton id="option1" name="option" value="초과근무" v-model="radioValue" />
                <label for="option1" class="leading-none ml-2">초과근무</label>
            </div>
            <div class="flex items-center">
                <RadioButton id="option2" name="option" value="해피휴무" v-model="radioValue" />
                <label for="option2" class="leading-none ml-2">해피휴무</label>
            </div>
        </div>
        <div class="flex justify-end">
            <Button type="button" class="mr-2 mb-2" label="Search" icon="pi pi-search" @click="getOvertimeStatus()" @keyup.enter="getOvertimeStatus()" />
            <!-- <Button type="button" class="mr-2 mb-2" label="Search" icon="pi pi-search" :loading="loading[0]" @click="load(0)" /> -->
        </div>
    </div>

    <div class="card">
        <div class="font-semibold text-xl mb-4">조회</div>
        <DataTable v-if="isOvertime" :value="overtimeStatusList" paginator :rows="10" :rowsPerPageOptions="[10, 20, 50, 100]" :size="size.value" removableSort stripedRows showGridlines tableStyle="min-width: 50rem">
            <Column field="name" header="이름" sortable style="width: 15%"></Column>
            <Column field="dept" header="사업부" sortable style="width: 15%"></Column>
            <Column field="overtimeDate" header="날짜" sortable style="width: 15%"></Column>
            <Column field="overtimeHours" header="시간" sortable style="width: 10%"></Column>
            <Column field="comment" header="코멘트" sortable style="width: 45%"></Column>
        </DataTable>
        <DataTable v-else :value="dayOffStatusList" paginator :rows="10" :rowsPerPageOptions="[10, 20, 50, 100]" :size="size.value" removableSort stripedRows showGridlines tableStyle="min-width: 50rem">
            <Column field="name" header="이름" sortable style="width: 15%"></Column>
            <Column field="dept" header="사업부" sortable style="width: 15%"></Column>
            <Column field="dayOffDate" header="휴무 날짜" sortable style="width: 15%"></Column>
            <Column field="comment" header="코멘트" sortable style="width: 55%"></Column>
        </DataTable>
    </div>
</template>
