// store/peopleStore.js
import { defineStore } from 'pinia';
import { ChatInfo } from '@/api/chat'
export const useToDotStore = defineStore('message', {
    state: () => ({
        todoList: [] as ChatInfo[],
    }),
    actions: {
        addToDoList(message: ChatInfo) {
            this.todoList.push(message);
        },
        updateToDo(message: ChatInfo[]) {
            this.todoList = message;
        }
    }
});