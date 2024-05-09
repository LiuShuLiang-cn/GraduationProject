// store/peopleStore.js
import { defineStore } from 'pinia';
import { DeployItem, NumberOfPeopleItem, OperateInfo } from '@/api/websocket'
export const useWebsocketStore = defineStore('websocket', {
    state: () => ({
        deployList: [] as DeployItem[],
        numberOfPeopleList: [] as NumberOfPeopleItem[],
        operate: {} as OperateInfo,
    }),
    actions: {
        updateNumberOfPeopleList(newList: NumberOfPeopleItem[]) {
            this.numberOfPeopleList = newList;
        },
        // 这里添加另外两个方法来更新 deployList 和 operate
        updateDeployList(newList: DeployItem[]) {
            this.deployList = newList;
        },
        updateOperate(newOperate: OperateInfo) {
            this.operate = newOperate;
        }
    }
});