import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
import * as store from '@/store'
import * as chart from './components/chart'
import '@/assets/css/index.scss'
import 'element-plus/dist/index.css'
import axios from 'axios'

axios.defaults.baseURL = 'http://127.0.0.1:8015'
axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded'

createApp(App)
    .use(chart)
    .use(store)
    .use(router)
    .mount('#app')
