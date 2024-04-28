import { createRouter, createWebHistory } from "vue-router";


const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            // component: () => import('@/components/RightSider.vue')
            redirect: '/login'
        },
        {
            path: "/login",
            name: 'Login',
            component: () => import('@/views/Login.vue'),
        }, {
            path: '/index',
            name: 'Index',
            component: () => import('@/views/Index.vue')
        }, {
            path: '/map',
            name: 'Gmap',
            component: () => import('@/components/Gmap.vue')
        }, {
            path: '/right',
            name: 'rightSider',
            component: () => import('@/components/RightSider.vue')
        }
    ],
});
export default router;
