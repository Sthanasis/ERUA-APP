import { createApp } from 'vue';

import pinia from './pinia';
import vuetify from './vuetify';
import router from './router';

import { VBtn } from 'vuetify/components';
import App from '~/App.vue';

import VCalendar from 'v-calendar';
import 'v-calendar/style.css';
import './index.css';

const app = createApp(App);

app.component('VBtn', VBtn);

app.use(pinia).use(router).use(vuetify).use(VCalendar, {}).mount('#app');
