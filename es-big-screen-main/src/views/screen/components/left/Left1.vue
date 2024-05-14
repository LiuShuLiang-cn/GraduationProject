<template>
    <div class="es-block">
        <Title>地区销量趋势</Title>
        <div style="width: 100%;height: 90%;">
            <Chat :systemId="props.systemId" :role="props.role"></Chat>
        </div>
    </div>
</template>

<script setup lang='ts'>
import { ref, onMounted, defineProps } from 'vue'
import Title from '../Title.vue'
import Chat from '@/components/message/chat.vue'
import { websocket, connectWebSocket } from '@/utils/websocket';
import { ElMessage } from 'element-plus';
import { ChatInfo } from '@/api/chat';
const props = defineProps({
    systemId: String,
    role: String
})
onMounted(() => {
    // initWebSocket()
})

</script>

<style lang='scss' scoped>
.es-block {
    width: 100%;
    height: 100%;
}
</style>
