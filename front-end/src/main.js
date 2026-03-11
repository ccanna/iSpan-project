import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.min.css'
import './assets/styles/custom.scss'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import Prime from './plugins/primevue.js';


const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Prime)

app.mount('#app')


