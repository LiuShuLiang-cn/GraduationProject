import { defineStore } from 'pinia'

interface ScreenState {
    title: string,
    theme: 'dark' | 'light'
}

export const useScreenStore = defineStore({
    id: 'screen',
    state: (): ScreenState => {
        return {
            title: '虚拟仿真试验',
            theme: 'light'
        }
    }
})
