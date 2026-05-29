<script setup>
import { ref } from 'vue';

import AppMenuItem from './AppMenuItem.vue';
const model = ref([
    {
        label: 'Happy Friday',
        items: [
            { label: 'Happy Friday 관리', icon: 'pi pi-fw pi-clock', to: '/register/overtime' },
            {
                label: 'Happy Friday 조회',
                icon: 'pi pi-fw pi-search',
                to: '/select/overtime'
            },
            {
                label: 'Happy Friday 보고서',
                icon: 'pi pi-fw pi-chart-bar',
                items: [
                    
                    {
                        label: '분기별 고객사 보고',
                        icon: 'pi pi-fw pi-table',
                        to: '/customer/report'
                    }
                ]
            }
        ]
    }
]);
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
