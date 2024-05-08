import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Screen from '@/views/screen/index.vue'
import Map from '@/views/screen/components/center/AMap.vue'
const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/screen'
    },
    {
        path: '/screen',
        component: Screen
    },
    {
        path: '/map',
        component: Map
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
