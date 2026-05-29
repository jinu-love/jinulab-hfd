<script setup>
import { apiClient } from '@/api/auth.js';
import { useAuthStore } from '@/stores/auth';
import moment from 'moment';
import { storeToRefs } from 'pinia';
import { onMounted, ref, watch } from 'vue';
const calendarValue = ref();
const dropdownValue = ref(null);
const authStore = useAuthStore;
const comment = ref(null);
const radioValue = ref(null);
const timeValues = ref([
    { name: '0.5', code: '0.5' },
    { name: '1', code: '1' },
    { name: '1.5', code: '1.5' },
    { name: '2', code: '2' },
    { name: '2.5', code: '2.5' },
    { name: '3', code: '3' },
    { name: '3.5', code: '3.5' },
    { name: '4', code: '4' },
    { name: '4.5', code: '4.5' },
    { name: '5', code: '5' },
    { name: '5.5', code: '5.5' },
    { name: '6', code: '6' },
    { name: '6.5', code: '6.5' },
    { name: '0', code: '0' }
]);

const isflag = ref(false);
const registTypeValue = ref(null);
const overtimeList = ref(null);

const thisMonth = ref(null);
const thisMonthOvertime = ref(null);
const thisDayOffDate = ref(null);

// axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('jwt-token');
// const userInfo = JSON.parse(localStorage.getItem('user-info'));

// ✅ storeToRefs 사용 → 반응성 유지
const { userInfo, isAuthenticated, isAdmin } = storeToRefs(useAuthStore());

onMounted(() => {
    radioValue.value = '초과근무';
    getOvertimeHistory();
    getThisMonthOvertime();
});

const getThisMonthOvertime = async () => {
    try {
        const response = await apiClient.get('/v1/get/this/month/overtime', {
            params: {
                accountId: userInfo.value.accountId
            }
        });
        thisMonthOvertime.value = response.data.overtime;
        thisMonth.value = response.data.thisMonth;
        thisDayOffDate.value = response.data.dayOffDate;
    } catch (error) {
        console.error('getThisMonthOvertime - error', error);
    }
};

const getOvertimeHistory = async () => {
    try {
        const response = await apiClient.get('/v1/get/overtime/history', {
            params: {
                accountId: userInfo.value.accountId
            }
        });
        overtimeList.value = response.data.overtimeList;
    } catch (error) {
        console.error('getOvertimeHistory - error', error);
    }
};

const saveOvertime = async () => {
    try {
        if (isflag.value == false) {
            const response = await apiClient.post('/v1/save/overtime', {
                accountId: userInfo.value.accountId,
                overtimeDate: moment(calendarValue.value).format('YYYYMMDD'),
                overtimeHours: dropdownValue.value.name,
                comment: comment.value,
                //2026-04-23 L.JW
                name: userInfo.value.name,
                company: userInfo.value.company,
                dept: userInfo.value.department,
                team: userInfo.value.team,
                part: userInfo.value.part,
                rank: userInfo.value.rank,
                position: userInfo.value.position
            });
            alert('초과근무 시간을 정상적으로 등록하였습니다.');
        } else {
            const response = await apiClient.post('/v1/save/dayoff', {
                accountId: userInfo.value.accountId,
                dayoffDate: moment(calendarValue.value).format('YYYYMMDD'),
                dayoffType: radioValue.value,
                comment: comment.value,
                //2026-04-23 L.JW
                name: userInfo.value.name,
                company: userInfo.value.company,
                dept: userInfo.value.department,
                team: userInfo.value.team,
                part: userInfo.value.part,
                rank: userInfo.value.rank,
                position: userInfo.value.position
            });
            alert('해프휴무 날짜를 정상적으로 등록하였습니다.');
        }
    } catch (error) {
        console.error('saveOvertime error', error);
        alert(error.response.data.code + ': ' + error.response.data.description);
    }

    getOvertimeHistory();
    getThisMonthOvertime();
};

watch(radioValue, (currentValue, prevValue) => {
    if (radioValue.value === '초과근무') {
        isflag.value = false;
        console.info(radioValue);
    } else {
        isflag.value = true;
        console.info(radioValue);
    }
});
</script>

<template>
    <div class="grid grid-cols-6 grid-rows-1 gap-4">
        <!-- <div class="col-span-6 lg:col-span-6 xl:col-span-3">
            <div class="card flex flex-col gap-4">
                <div class="col-span-12 lg:col-span-6 xl:col-span-3">
                    <div class="font-semibold text-xl">Happy Friday 현황</div>
                    <div class="flex justify-between mb-4">
                        <div>
                            <span class="font-semibold text-xl">{{}} {{ userInfo.name || authStore.mail }}</span>
                            <span class="font-semibold text-xl">{{}} {{ userInfo.rank }}님</span>
                        </div>
                    </div>
                    <div>
                        <span class="text-muted-color">이번 달 초과근무 시간: </span>
                        <span class="text-primary font-medium">{{ thisMonthOvertime }} 시간</span>
                    </div>
                    <div>
                        <span class="text-muted-color">이번 달 해프휴무 날짜: </span>
                        <span class="text-primary font-medium">{{ thisMonthOvertime }} 시간</span>
                    </div>
                </div>
            </div>
        </div> -->

        <div class="col-span-6 lg:col-span-6 xl:col-span-3">
            <div class="card flex flex-col gap-4">
                <!-- <div class="font-semibold text-xl">Happy Friday 이번 달 현황</div> -->
                <div class="font-semibold text-xl">Happy Friday 이번 달 현황</div>
                <div class="font-semibold text-l">{{ userInfo.dept }} {{ userInfo.name }} {{ userInfo.rank }}님</div>
                <!-- <label for="firstname2">{{ userInfo.dept }} {{ userInfo.name }} {{ userInfo.rank }}님</label> -->
                <!-- <label for="firstname2">해프휴무 날짜:</label> -->
                <div>
                    <label for="firstname2">초과근무 시간: </label>
                    <span class="text-primary font-medium"> {{ thisMonthOvertime }} 시간</span>
                </div>
                <div>
                    <label for="firstname2">해프휴무 날짜: </label>
                    <span class="text-primary font-medium"> {{ thisDayOffDate }}</span>
                </div>
            </div>
        </div>
        <div class="col-span-6 lg:col-span-6 xl:col-span-3">
            <div class="card flex flex-col gap-4">
                <div class="font-semibold text-xl">Happy Friday 등록</div>
                <label for="firstname2">유형</label>
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex items-center">
                        <RadioButton id="option1" name="option" value="초과근무" v-model="radioValue" />
                        <label for="option1" class="leading-none ml-2">초과근무</label>
                    </div>
                    <div class="flex items-center">
                        <RadioButton id="option2" name="option" value="해피휴가" v-model="radioValue" />
                        <label for="option2" class="leading-none ml-2">해프휴무</label>
                    </div>
                    <div class="flex items-center">
                        <RadioButton id="option3" name="option" value="휴가" v-model="radioValue" />
                        <label for="option3" class="leading-none ml-2">해프휴무(휴가사용)</label>
                    </div>
                </div>

                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="firstname2">날짜</label>
                        <DatePicker v-model="calendarValue" model-type="format" format="yyyy-MM-dd" rows="3" :showIcon="true" dateFormat="yy-mm-dd DD" :showButtonBar="true" class="w-full"></DatePicker>
                    </div>
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="firstname2">시간</label>
                        <Select v-model="dropdownValue" :options="timeValues" optionLabel="name" placeholder="Select" class="w-full" :disabled="isflag" />
                    </div>
                </div>
                <div class="flex flex-col md:flex-row gap-4">
                    <div class="flex flex-wrap gap-2 w-full">
                        <label for="address">메모</label>
                        <Textarea id="address" rows="2" v-model="comment" class="w-full" />
                    </div>
                </div>
                <Button @click="saveOvertime()" label="저장" severity="success" style="width: 120px" />
            </div>
        </div>

        <div class="col-span-6 lg:col-span-6 xl:col-span-6">
            <div class="card flex flex-col gap-4">
                <div class="font-semibold text-xl">Happy Friday 기록</div>
                <DataTable :value="overtimeList" paginator stripedRows :rows="5" :rowsPerPageOptions="[5, 10, 20, 50]" tableStyle="min-width: 10rem">
                    <template #paginatorstart>
                        <Button type="button" icon="pi pi-refresh" text />
                    </template>
                    <template #paginatorend>
                        <Button type="button" icon="pi pi-download" text />
                    </template>
                    <Column field="overtimeDate" header="날짜" style="width: 30%"></Column>
                    <Column field="overtimeHours" header="시간" style="width: 20%"></Column>
                    <Column field="comment" header="코멘트" style="width: 50%"></Column>
                </DataTable>
            </div>
        </div>
    </div>
</template>
