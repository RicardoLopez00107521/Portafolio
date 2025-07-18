import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import vue3GoogleLogin from 'vue3-google-login';

const googleClientId = '665325527090-eppkp6kremu3sio9pk9d3bakmvh3g892.apps.googleusercontent.com';

import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

const vuetify = createVuetify({
  components,
  directives,
});

// Toastification
import Toast, { POSITION } from "vue-toastification";
import "vue-toastification/dist/index.css";

const app = createApp(App);

app.use(vue3GoogleLogin, {
  clientId: googleClientId,
});

app.use(router);
app.use(vuetify);
app.use(Toast, {
  position: POSITION.TOP_RIGHT,
  timeout: 2000
});

app.mount('#app');
