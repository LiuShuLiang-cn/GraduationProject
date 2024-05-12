<template>
    <div class="es-block">
        <Title>商家销售统计</Title>
        <div style="width: 100%;height: 90%;">
            <ToDo />
        </div>
    </div>
</template>

<script setup lang='ts'>
import { ref } from 'vue'
import Title from '../Title.vue'
import Chart from '@/components/chart/Chart.vue'
import allData from '@/assets/data/seller.json'
import * as echarts from 'echarts'
import ToDo from "@/components/message/todo.vue";


</script>

<style lang='scss' scoped>
.es-block {
    width: 100%;
    height: 100%;
}
</style>
