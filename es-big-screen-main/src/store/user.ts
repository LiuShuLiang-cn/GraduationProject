import { User } from "@/api/user";
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            // 用户信息初始值
            info: null,
        }
    },
    actions: {
        // 设置用户信息
        setUserInfo(userInfo) {
            this.info = userInfo;
        },
        // 清除用户信息
        clearUserInfo() {
            this.info = null;
        }
    }
})