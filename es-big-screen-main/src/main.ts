import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
import * as store from '@/store'
import * as chart from './components/chart'
import '@/assets/css/index.scss'
import 'element-plus/dist/index.css'
import axios from 'axios'
import VueVideoPlayer from '@videojs-player/vue'
import 'video.js/dist/video-js.css'
axios.defaults.baseURL = 'http://127.0.0.1:8015'
axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded'
// import { createPinia } from "pinia"
// const pinia = createPinia()
createApp(App)
    .use(chart)
    .use(VueVideoPlayer)
    .use(store)
    // .use(pinia)
    .use(router)
    .mount('#app')
