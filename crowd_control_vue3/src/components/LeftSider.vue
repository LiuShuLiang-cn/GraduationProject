<template>
    <el-aside>
        <div class="section">
            <!--插入图标-->
            <v-mini-weather-icon :icon="icon"></v-mini-weather-icon>
            <!--DIY内容-->
            <span style="text-align: center;width: 100%;">{{ weather.cityName }}/{{ weather.weather }}/{{ weather.temp
                }}</span>
        </div>
        <div class="section">
            <PeopleChart :systemId="systemId" :role="role" />
        </div>
        <div class="section">
            <PeopleChart :systemId="systemId" :role="role" />
        </div>
    </el-aside>
</template>
<script setup lang="ts">
import { vMiniWeatherIcon } from 'vue3-mini-weather'
import { onMounted, ref, defineProps } from 'vue'
import PeopleChart from "@/components/chart/PeopleChart.vue";
const weather = ref({
    cityName: "西湖",
    weather: "晴天",
    temp: "25°"
})
const icon = ref('d00')
const props = defineProps({
    systemId: String,
    role: String
})

onMounted(() => {
    getWeather()
})

function getWeather() {
    // TODO 获取天气
}
</script>

<style>
.el-aside {
    display: flex;
    /* 使用Flexbox布局 */
    flex-direction: column;
    /* 子元素垂直排列 */
}

.section {
    flex: 1;
    /* 子元素占据相同的空间 */
    display: flex;
    /* 每个section也使用Flexbox布局 */
    align-items: center;
    /* 垂直居中 */
    justify-content: center;
    /* 水平居中 */
    text-align: center;
    /* 确保文本居中，尽管这可能不是必需的，因为flex布局已经居中了 */
    border: 1px solid #e8e8e8;
    /* 为了可视化分隔 */
    margin: 5px;
    /* 各section之间添加一些间距 */
    box-sizing: border-box;
    /* 包含边框在尺寸内 */
}

/* 如果需要为图标和文字添加一些间距 */
.section>span {
    margin-left: 10px;
}
</style>