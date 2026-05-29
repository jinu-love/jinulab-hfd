<script setup>
import { RouterView } from 'vue-router';
</script>

<template>
    <router-view />
</template>

<style scoped></style>
<style>


    html body .p-calendar .p-inputtext {
        font-size: 16px !important;
    }

    /* 위에서도 안되면 아래 추가 */
    html body input.p-inputtext {
        font-size: 16px !important;
    }

</style>