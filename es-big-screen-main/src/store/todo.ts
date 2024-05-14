// store/peopleStore.js
import { defineStore } from 'pinia';
import { Message } from '@/api/message'
export const useToDotStore = defineStore('message', {
    state: () => ({
        todoList: [] as Message[],
    }),
    actions: {
        addToDoList(message: Message) {
            this.todoList.push(message);
        },
        updateToDo(message: Message[]) {
            this.todoList = message;
        }
    }
});