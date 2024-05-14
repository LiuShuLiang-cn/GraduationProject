<template>
    <div id="todo">
        <ul v-if="tasks.length > 0" class="task-list">
            <li v-for="(task, index) in todos" :key="index" class="task-item">
                <!-- <span class="task-text">{{ task.text }}——{{ task.fromRole }}</span>
                <el-button type="danger" @click="removeTodo(index)">删除</el-button> -->
                <div class="content">
                    <span class="task-text">{{ task.text }}——{{ task.fromRole }}</span>
                </div>
                <div class="actions">
                    <el-button type="danger" class="delete-button" @click="removeTodo(index)">删除</el-button>
                </div>
            </li>
        </ul>
    </div>
</template>

<script lang="ts" setup>

import { defineComponent, ref, onMounted } from 'vue';
import { ElButton, ElInput, ElNotification } from 'element-plus'
import { Message } from "@/api.message";
import axios from 'axios'
const newTask = ref('');
const tasks = ref<Message[]>([]);
import { useToDotStore } from "@/store/todo.ts";
const todos = useToDotStore()
onMounted(() => {
    axios.get("/command", {
        params: {
            systemId: '25',
            roleTopic: '公安'
        }
    }).then((res) => {
        console.log(res.data)
        if (res.data.length != 0) {
            ElNotification({
                title: 'Warning',
                message: '你有未完成的任务',
                type: 'warning',
            })
        }
        todos.updateToDo(res.data)
    })
})

</script>

<style scoped>
#todo {
    text-align: center;
    margin-top: 60px;
    /* 注意这里存在冲突，下面又重置了margin */
    list-style: none;
    margin: 0;
    /* 这里重置了margin-top，请确认这是否是您的意图 */
    padding: 0;
    height: 100%;
    overflow-y: scroll;
    scrollbar-width: none;
    /* Firefox */
    -ms-overflow-style: none;
    /* Internet Explorer 10+ */
}

#todo::-webkit-scrollbar {
    display: none;
    /* Chrome, Safari, Opera */
}

.task-list {
    list-style-type: none;
    padding-left: 0;
}

.task-item {
    border: 2px solid #42b983;
    /* Element UI 主题色边框 */
    border-radius: 8px;
    /* 圆角边框 */
    padding: 10px 20px;
    /* 内部填充 */
    margin: 10px 0;
    /* 防止文本相互紧贴 */
    background-color: #f0f9eb;
    /* 轻淡的背景色 */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    /* 轻微的阴影以创建层次感 */
    transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
    /* 平滑的过渡效果 */
}

.task-item:hover {
    transform: translateY(-2px);
    border-bottom-color: transparent;
}

.task-text {
    font-size: 20px;
    flex-grow: 1;
    margin-right: 10px;
}

.delete-button {
    transition: transform 0.3s ease;
}

.delete-button:hover {
    transform: scale(1.05);
    /* 鼠标悬停在删除按钮上时放大 */
}

.content {
    flex-grow: 1;
    margin-right: 10px;
    text-align: left;
}

.actions {
    flex-shrink: 0;
    text-align: right;
}

.delete-button {
    /* 这个样式根据您的需要调整按钮大小 */
    width: 60px;
    /* 修改按钮的宽度 */
}
</style>