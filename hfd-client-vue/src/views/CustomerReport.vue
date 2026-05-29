<script setup>
import { apiClient } from '@/api/auth.js';
import moment from 'moment';
import { ref } from 'vue';
import * as XLSX from 'xlsx';

const customers = ref();
const selectedCustomers = ref();
const filters = ref();
const userInfo = JSON.parse(localStorage.getItem('user-info'));
const reportList = ref(null);
const customerReportExcel = ref(null);

const getCustomerReport = async () => {
    try {
        const response = await apiClient.get('v1/get/customer/report', {
            params: {
                fromDate: moment(dates.value[0]).format('YYYYMMDD'),
                toDate: moment(dates.value[1]).format('YYYYMMDD'),
                dept: deptValue.value.name
            }
        });
        reportList.value = response.data.customerReportDataList;
    } catch (error) {
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

const deptValues = ref([
    { name: 'ITO 사업부', code: 'ITO 사업부' },
    { name: 'SI 사업부', code: 'SI 사업부' }
]);

const rows123 = [
    { name: '홍길동', age: 30, city: '서울' },
    { name: '김철수', age: 28, city: '부산' },
    { name: '이영희', age: 35, city: '대구' }
];

const exportToExcel = async () => {
    try {
        const response = await apiClient.get('v1/get/customer/report/excel', {
            params: {
                fromDate: moment(dates.value[0]).format('YYYYMMDD'),
                toDate: moment(dates.value[1]).format('YYYYMMDD'),
                dept: deptValue.value.name
            }
        });
        customerReportExcel.value = response.data.customerReportDataList;
    } catch (error) {
        console.error('getOvertimeStatus error', error);
        alert(error.response.data.code + ': ' + error.response.data.description);
    }

    // 1) JSON 데이터를 워크시트로 변환
    const worksheet = XLSX.utils.json_to_sheet(customerReportExcel.value);

    // 2) 새 워크북 생성 후 워크시트 추가
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');

    // 3) 파일로 저장 (브라우저 다운로드)
    XLSX.writeFile(workbook, 'example.xlsx');
};

const date = ref();
const dates = ref();
const deptValue = ref();
</script>

<template>
    <div class="card flex flex-col gap-4">
        <div class="font-semibold text-xl">조건</div>

        <div class="flex flex-col md:flex-row gap-4">
            <FloatLabel variant="on">
                <DatePicker v-model="dates" selectionMode="range" view="month" dateFormat="yy/mm" :manualInput="false" />
                <label for="over_label">월 범위 선택</label>
            </FloatLabel>

            <Select v-model="deptValue" :options="deptValues" optionLabel="name" placeholder="부서" />
        </div>
        <div class="flex justify-end">
            <Button type="button" class="mr-2 mb-2" label="Search" icon="pi pi-search" @click="getCustomerReport()" @keyup.enter="getCustomerReport()" />
            <!-- <Button type="button" class="mr-2 mb-2" label="Search" icon="pi pi-search" :loading="loading[0]" @click="load(0)" /> -->
        </div>
    </div>

    <div class="card">
        <div class="font-semibold text-xl mb-4">조회</div>

        <Toolbar class="mb-6">
            <template #end>
                <Button label="Export" icon="pi pi-upload" severity="secondary" @click="exportToExcel()" />
            </template>
        </Toolbar>
        <DataTable :value="reportList" :size="small" resizableColumns columnResizeMode="fit" scrollable scrollHeight="400px" class="mt-6" removableSort stripedRows showGridlines>
            <!-- <DataTable :value="reportList" paginator :rows="10" :rowsPerPageOptions="[10, 20, 50, 100]" :size="size.value" removableSort stripedRows showGridlines tableStyle="min-width: 50rem"> -->
            <Column field="company" header="회사" style="min-width: 128px" frozen class="font-bold"></Column>
            <Column field="name" header="이름" style="min-width: 70px" frozen class="font-bold"></Column>
            <Column field="month" header="월" style="min-width: 40px" frozen class="font-bold"></Column>
            <Column field="total" header="Total" style="min-width: 3rem" bodyStyle="text-align:center"></Column>
            <Column field="d1" header="1일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d2" header="2일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d3" header="3일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d4" header="4일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d5" header="5일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d6" header="6일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d7" header="7일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d8" header="8일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d9" header="9일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d10" header="10일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d11" header="11일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d12" header="12일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d13" header="13일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d14" header="14일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d15" header="15일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d16" header="16일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d17" header="17일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d18" header="18일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d19" header="19일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d20" header="20일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d21" header="21일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d22" header="22일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d23" header="23일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d24" header="24일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d25" header="25일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d26" header="26일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d27" header="27일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d28" header="28일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d29" header="29일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d30" header="30일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
            <Column field="d31" header="31일" style="min-width: 60px" bodyStyle="text-align:center"></Column>
        </DataTable>
    </div>
</template>
