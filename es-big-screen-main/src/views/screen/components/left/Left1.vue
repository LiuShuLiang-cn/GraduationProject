<template>
    <div class="es-block">
        <Title>消息交互区</Title>
        <div style="width: 100%;height: 90%;">
            <Chat :systemId="props.systemId" :role="props.role"></Chat>
        </div>
    </div>
</template>

<script setup lang='ts'>
import { onMounted, defineProps } from 'vue'
import Title from '../Title.vue'
import Chat from '@/components/message/chat.vue'
const props = defineProps({
    systemId: String,
    role: String
})
onMounted(() => {
})

</script>

<style lang='scss' scoped>
.es-block {
    width: 100%;
    height: 100%;
}
</style>
