<template>
    <div class="es-block">
        <Title>地区人流图</Title>
        <div style="width: 100%;height: 90%;">
            <Chart :option="option" />
        </div>
    </div>
</template>

<script setup lang='ts'>
import { computed } from 'vue'
import Title from '../Title.vue'
import Chart from '@/components/chart/Chart.vue'
import * as echarts from 'echarts'
import { useWebsocketStore } from '@/store/websocket'
import { NumberOfPeopleItem } from '@/api/websocket'
const webSocketStore = useWebsocketStore()

let x: number = 0
let xData = []
let yData = [
    { name: '钱塘里', data: [] },
    { name: '长生里', data: [] },
    { name: '劝业里', data: [] },
    { name: '学士里', data: [] },
    { name: '龙翔里', data: [] },
    { name: '仁和里', data: [] },
    { name: '东坡里', data: [] },
    { name: '将军里', data: [] },
    { name: '泗水里', data: [] }
]

const option = computed(() => {

    // 格式化数据以符合ECharts的期望格式
    let peoples = webSocketStore.numberOfPeopleList
    // @ts-ignore
    xData.push(x++)
    // 找到对应的region对象
    // 使用forEach循环更新每个地方对应的人口值。
    peoples.forEach((newData: NumberOfPeopleItem) => {
        let peopleObject = yData.find(people => people.name === newData.region);
        // 如果找到，更新对应的data值。
        if (peopleObject) {
            // @ts-ignore
            peopleObject.data.push(newData.number);
        }

    });
    return {
        grid: {
            left: '3%',
            top: '25%',
            right: '4%',
            bottom: '1%',
            containLabel: true
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            left: 20,
            top: '8%',
            icon: 'circle',
            data: yData,
            textStyle: {
                color: '#aaa'
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xData
        },
        yAxis: {
            type: 'value'
        },
        series: getSeries()
    }
})


function getSeries() {
    // 半透明的颜色值
    const colorArr1 = [
        'rgba(11, 168, 44, 0.5)',
        'rgba(33, 185, 33, 0.5)',
        'rgba(44, 110, 255, 0.5)',
        'rgba(22, 242, 217, 0.5)',
        'rgba(254, 33, 30, 0.5)',
        'rgba(11, 168, 44, 0.5)',
        'rgba(11, 168, 44, 0.5)',
        'rgba(11, 168, 44, 0.5)',
        'rgba(250, 105, 0, 0.5)'
    ]
    // 全透明的颜色值
    const colorArr2 = [
        'rgba(11, 168, 44, 0)',
        'rgba(44, 110, 255, 0)',
        'rgba(22, 242, 217, 0)',
        'rgba(254, 33, 30, 0)',
        'rgba(250, 105, 0, 0)',
        'rgba(11, 168, 44, 0.5)',
        'rgba(11, 168, 44, 0.5)',
        'rgba(11, 168, 44, 0.5)',
        'rgba(11, 168, 44, 0.5)',
    ]
    // y轴的数据 series下的数据
    const valueArr = yData
    const seriesArr = valueArr.map((item, index) => {
        return {
            name: item.name,
            type: 'line',
            data: item.data,
            stack: 'map',
            itemStyle: {
                borderWidth: 4
            },
            lineStyle: {
                width: 3
            },
            symbolSize: 0,
            symbol: 'circle',
            smooth: true,
            areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                        offset: 0,
                        color: colorArr1[index]
                    }, // %0的颜色值
                    {
                        offset: 1,
                        color: colorArr2[index]
                    } // 100%的颜色值
                ])
            }
        }
    })

    return seriesArr
}


</script>

<style lang='scss' scoped>
.es-block {
    width: 100%;
    height: 100%;
}
</style>
