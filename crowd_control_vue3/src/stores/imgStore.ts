// 主要用于存放图片
import { defineStore } from 'pinia'

import registerImg from '@/assets/img/log.svg'
import logImg from '@/assets/img/log.svg'

export const useImgStore = defineStore(
    'imgStore',
    {
        state() {
            return {
                imgs: [
                    registerImg, //0
                    logImg, //1
                ],
            }
        }
    }
)

