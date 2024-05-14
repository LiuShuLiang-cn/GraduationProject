import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Screen from '@/views/screen/index.vue'
import Map from '@/views/screen/components/center/AMap.vue'
import Login from '@/views/screen/login.vue'
const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/screen',
        component: Screen
    },
    {
        path: '/map',
        component: Map
    }, {
        path: '/login',
        component: Login
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
