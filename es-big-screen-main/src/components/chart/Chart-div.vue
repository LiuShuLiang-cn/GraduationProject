<template>
    <div class="mainCLo">
        <div ref="chartRef" className='es-chart'></div>
    </div>
</template>

<script setup lang='ts'>
import { onMounted, PropType, shallowRef, watch } from 'vue'
import * as echarts from 'echarts'
import { ECharts, EChartsCoreOption } from 'echarts'

const props = defineProps({
    option: {
        type: Object as PropType<EChartsCoreOption>,
        required: true,
        default: () => ({})
    },
    loading: Boolean
})
const chartRef = shallowRef<HTMLElement | null>(null)

const chart = shallowRef<ECharts | null>(null)
function init() {
    if (props.option) {
        chart.value = echarts.init(chartRef.value!)
        setOption(props.option)
    }
}
function setOption(option, notMerge?: boolean, lazyUpdate?: boolean) {
    chart.value!.setOption(option, notMerge, lazyUpdate)
}

function resize() {
    chart.value!.resize()
}

watch(() => props.option, (oldValue, newValue) => {
    setOption(props.option)
})

// show loading
watch(() => props.loading, (val) => {
    if (!chart.value) return
    if (val) {
        chart.value!.showLoading()
    } else {
        chart.value!.hideLoading()
    }
})

onMounted(() => {
    init()
})

defineExpose({
    chart,
    setOption,
    resize
})
</script>

<style lang='scss' scoped>
.mainCLo {
    width: 300px;
    height: 300px;
    position: relative;
    top: -40px;
    display: flex;
    justify-content: center;
    align-items: center;

}

.es-chart {
    // width: 100%;
    // height: 100%;
    position: relative;
    top: -10px;
    left: 20%;
    width: 250px;
    height: 300px;
}
</style>
