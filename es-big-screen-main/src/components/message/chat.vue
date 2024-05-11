<template>
    <div class="chat-container">
        <div class="inputs-container">
            <input v-model="message" type="text" placeholder="Enter your message here" />
            <input v-model="recipient" type="text" placeholder="Recipient" />
            <select v-model="messageType">
                <option disabled value="">Please select one</option>
                <option>Text</option>
                <option>Image</option>
                <option>Video</option>
            </select>
            <button @click="sendMessage">Send</button>
        </div>
        <div class="messages-container">
            <div v-for="(message, index) in messages" :key="index">
                <p><strong>To:</strong> {{ message.recipient }} </p>
                <p><strong>Type:</strong> {{ message.type }} </p>
                <p>{{ message.content }}</p>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { defineComponent, ref } from 'vue';


const message = ref('');
const recipient = ref('');
const messageType = ref('');
const messages = ref([] as Array<{ recipient: string, type: string, content: string }>);

function sendMessage() {
    if (message.value.trim() && recipient.value.trim() && messageType.value) {
        messages.value.push({
            recipient: recipient.value,
            type: messageType.value,
            content: message.value
        });
        message.value = '';
        recipient.value = '';
        messageType.value = '';
    } else {
        alert("All fields are required");
    }
};
</script>

<style scoped>
.chat-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 20px;
}

.inputs-container {
    margin-bottom: 20px;
}

.inputs-container input,
.inputs-container select {
    margin-right: 10px;
}

.messages-container div {
    border-bottom: 1px solid #ccc;
    margin-bottom: 10px;
}
</style>