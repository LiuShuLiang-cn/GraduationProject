<template>
    <div id="chat-window">
        <ul id="chat-log">
            <li v-for="(message, index) in messages" :key="index" :class="{ received: !message.fromMe }">
                <div class="message-header">{{ message.from }}</div>
                <div class="message-body">{{ message.text }}</div>
            </li>
        </ul>
        <div id="chat-input">
            <!-- 第一行：消息文本输入区域和消息类型选择 -->
            <div class="input-row">
                <textarea v-model="messageText"></textarea>
                <select v-model="msgType">
                    <option v-for="item in msgTypes" :key="item.value" :label="item.label" :value="item.value">文本
                    </option>
                </select>
            </div>
            <!-- 第二行：发送对象选择和发送按钮 -->
            <div class="input-row">
                <select v-model="toRole">
                    <option v-for="item in toRoles" :key="item.value" :label="item.label" :value="item.value" />
                </select>
                <button @click="sendMessage">发送</button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from "vue";
import { ElMessage } from 'element-plus';
import { ChatInfo } from '@/api/chat';
import { websocket, connectWebSocket } from '@/utils/websocket';

const messages = ref<Array<ChatInfo>>([]);
const messageText = ref<string>("");
const bol = ref(false);
const toRoles = ref()
const toRole = ref('')
const msgTypes = ref([{ value: "1", label: "指令" }, { value: "2", label: "普通消息" }, { value: "3", label: "群发" }])
const msgType = ref()
function sendMessage() {
    if (messageText.value) {
        if (bol.value) {
            return;
        }
        bol.value = true;
        const newMessage = {
            from: "Me",
            text: "",
            fromMe: true,
        };

        const newMessage1 = {
            from: "You",
            text: "狗",
            fromMe: false,
        };

        messages.value.push(newMessage1);
        messages.value.push(newMessage);
        const originalText = messageText.value;

        scrollToBottom(); // 先滚动到底部

        nextTick(() => {
            // 定时器每200ms将消息一个一个字符地显示出来
            let currentIndex = 0;
            const timer = setInterval(() => {
                if (currentIndex >= originalText.length) {
                    clearInterval(timer);
                    nextTick(() => {
                        newMessage.text = originalText;
                        scrollToBottom(); // 再次滚动到底部
                        bol.value = false;
                    });
                    return;
                }
                const currentText = originalText.substring(0, currentIndex + 1);
                messages.value[messages.value.length - 1].text = currentText;
                scrollToBottom(); // 再次滚动到底部
                currentIndex++;
            }, 200);
        });
    }
}

function scrollToBottom() {
    const chatLog = document.getElementById("chat-log");
    if (chatLog) {
        chatLog.scrollTop = chatLog.scrollHeight;
    }
}
function initWebSocket() {
    connectWebSocket("ws:/127.0.0.1:8015/websocket/" + '25' + '/' + '指挥中心')
    websocket.onopen = function (event: any) {
        websocket.onopen = function (event: any) {
            ElMessage({ message: '连接成功', type: 'success' })
            var msg: ChatInfo = {
                id: String(25),
                fromRole: String(''),
                systemName: '',
                statue: '0',
                toRole: toRole.value,
                type: '4',
                text: props.role + '上线了',
                time: new Date().toString()
            }
            websocket.send(JSON.stringify(msg))
        }
    }
    websocket.onmessage = function (event: any) {
        const data: DataResponse = JSON.parse(event.data)
        const numberOfPeopleList = data.numberOfPeopleList
        websocketStore.updateDeployList(data.deployList)
        websocketStore.updateNumberOfPeopleList(numberOfPeopleList)
        websocketStore.updateOperate(data.operate)

    }
}
onMounted(() => {
    var r = [
        { value: "指挥中心", label: "指挥中心" },
        { value: "主办单位", label: "主办单位" },
        { value: "公安", label: "公安" },
        { value: "交警", label: "交警" },
        { value: "城管", label: "城管" },
        { value: "志愿者", label: "志愿者" },
        { value: "市民", label: "市民" },
        { value: "公交地铁", label: "公交地铁" },
    ]
    toRoles.value = r.filter(function (element) {
        return element.label !== 'props.role';
    });
    scrollToBottom(); // 每次更新聊天记录后滚动到底部
});
</script>

<style>
#chat-window {
    width: 100%;
    height: 100%;
    border: 1px solid #ccc;
    overflow: hidden;
    display: flex;
    flex-direction: column;
}

#chat-log {
    list-style: none;
    margin: 0;
    padding: 0;
    height: 100%;
    overflow-y: scroll;
    scrollbar-width: none;
    /* Firefox */
    -ms-overflow-style: none;
    /* Internet Explorer 10+ */
}

#chat-log::-webkit-scrollbar {
    display: none;
    /* Chrome, Safari, Opera */
}

#chat-log li {
    margin-bottom: 10px;
    padding: 5px;
    border-radius: 5px;
    display: flex;
    flex-direction: column;
    max-width: 100%;
    /* max-width: 80%; */
    align-items: flex-end;
}

.received {
    /* align-self: flex-end; */
    align-items: flex-start !important;
    background-color: #f5f5f5;
}



.message-header {
    font-size: 12px;
    color: #888;
    margin-bottom: 5px;
}

.message-body {
    font-size: 16px;
    color: #333;
    background-color: #1cc774;
    /* 保持不变，用于收到的消息 */
    border-radius: 8px;
    padding: 10px 15px;
    display: inline-block;
    max-width: 60%;
    word-wrap: break-word;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* 更新发送消息的背景色 */
.message-body.self {
    background-color: #9eec6e;
    /* 微信发送消息的浅绿色 */
    color: rgb(32, 182, 92);
    float: right;
    clear: both;
}


#chat-input {
    display: flex;
    flex-direction: column;
    padding: 10px;
    background-color: #f5f5f5;
}

.input-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

/* 移除最后一行的外边距 */
.input-row:last-child {
    margin-bottom: 0;
}

textarea,
select,
button {
    /* 设置统一高度和宽度 */
    height: 40px;
    width: calc(50% - 5px);
    /* 减去元素间距的一半保持间距 */
    padding: 5px;
    border: 1px solid #ccc;
    margin-right: 10px;
    /* 设置间距 */
    /* 文本区域样式调整 */
    resize: none;
    /* 禁止用户调整大小 */
}

button {
    padding: 5px;
    background-color: green;
    color: white;
    border: none;
    cursor: pointer;
}

/* 最后一个元素不需要右边距 */
.input-row>textarea:last-child,
.input-row>select:last-child,
.input-row>button:last-child {
    margin-right: 0;
}
</style>
