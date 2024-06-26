import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// import { viteMockServe } from 'vite-plugin-mock'
// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        // viteMockServe({
        //     supportTs: true,
        //     mockPath: './src/mock',
        // })
        // "vite-plugin-mock": "^2.9.6",
        // "mockjs": "^1.1.0",
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },

})
