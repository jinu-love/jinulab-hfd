import { createPinia } from 'pinia';
import { createApp } from 'vue';

import App from './App.vue';
import router from './router';

// PrimeVue & 테마 & 유틸
import '@/assets/styles.scss';
import Aura from '@primeuix/themes/aura';
import 'primeicons/primeicons.css';
import PrimeVue from 'primevue/config';
import ConfirmationService from 'primevue/confirmationservice'; // ✅ 추가 (필요 시)
import ToastService from 'primevue/toastservice'; // ✅ 추가



const app = createApp(App)
const pinia = createPinia()

// PrimeVue + Aura 테마 설정
app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            darkModeSelector: '.app-dark'
        }
    },
    ripple: true
})
app.use(ToastService)        // ✅ 추가
app.use(ConfirmationService) // ✅ 추가 (필요 시)
app.use(pinia)
app.use(router)
app.mount('#app')