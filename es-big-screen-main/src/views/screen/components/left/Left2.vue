<template>
    <div class="es-block">
        <Title>代办列表</Title>
        <div style="width: 100%;height: 90%;">
            <ToDo :systemId="props.systemId" :role="props.role" />
        </div>
    </div>
</template>

<script setup lang='ts'>
import { defineProps } from 'vue'
import Title from '../Title.vue'
import ToDo from "@/components/message/todo.vue";

const props = defineProps({
    systemId: String,
    role: String
})
</script>

<style lang='scss' scoped>
.es-block {
    width: 100%;
    height: 100%;
}
</style>
