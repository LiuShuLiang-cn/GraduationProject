<template>
    <div class="box" ref="chartDom"></div>
</template>
<script setup lang="ts">
import { ref, onMounted, defineProps, onUnmounted } from "vue";
import * as echarts from "echarts";
let chartDom = ref(null); //注意变量名 和 ref名字要对应
const props = defineProps({
    systemId: String,
    role: String
})
let intervalId: number
onMounted(() => {
    initChart();
    intervalId = setInterval(() => {
        initChart()
    }, 5000);

});
onUnmounted(() => {
    clearInterval(intervalId);
})
interface ADDR {
    label: string,
    value: {
        lng: number,
        lat: number
    }
}
const addrs = ref<ADDR[]>([
    {
        value: {
            lng: 120.160988,
            lat: 30.257241
        }, label: '钱塘里'
    },
    {
        value: {
            lng: 120.163306,
            lat: 30.256641
        }, label: '长生里'
    },
    {
        value: {
            lng: 120.161679,
            lat: 30.256041
        }, label: '劝业里'
    },
    {
        value: {
            lng: 120.162178,
            lat: 30.254551
        }, label: '学士里'
    },
    {
        value: {
            lng: 120.163452,
            lat: 30.254666
        }, label: '龙翔里'
    },
    {
        value: {
            lng: 120.162555,
            lat: 30.252937
        }, label: '仁和里'
    },
    {
        value: {
            lng: 120.163585,
            lat: 30.252126
        }, label: '东坡里'
    },
    {
        value: {
            lng: 120.162707,
            lat: 30.249315
        }, label: '将军里'
    },
    {
        value: {
            lng: 120.165169,
            lat: 30.249562
        }, label: '泗水里'
    },
    {
        value: {
            lng: 120.161503,
            lat: 30.253908
        }, label: '标记一'
    },
    {
        value: {
            lng: 120.160966,
            lat: 30.254118
        }, label: '标记二'
    },
    {
        value: {
            lng: 120.161199,
            lat: 30.253807
        }, label: '标记三'
    }
])
function getRegin(lng: number) {
    const matchedAddress = addrs.value.find((element: ADDR) => {
        return lng === element.value.lng;
    });
    return matchedAddress?.label;
}
const regions = ref<string[]>([])
const gaNums = ref([])
const jjNums = ref([])
const zyzNums = ref([])
const cgNums = ref([])
async function initChart() {
    while (regions.value.length > 0) {
        regions.value.pop();
    }
    while (gaNums.value.length > 0) {
        gaNums.value.pop()
    }
    while (jjNums.value.length > 0) {
        jjNums.value.pop()
    }
    while (cgNums.value.length > 0) {
        cgNums.value.pop()
    }
    while (zyzNums.value.length > 0) {
        zyzNums.value.pop()
    }
    var myChart = echarts.init(chartDom.value);
    await axios.get("/data/" + props.systemId).then((res) => {
        const deployList = res.data.deployList
        deployList.forEach((element: any, index: number) => {
            const region: string = getRegin(element.gaLng)
            regions.value.push(region)
            gaNums.value.push(element.ga)
            cgNums.value.push(element.cg)
            jjNums.value.push(element.jj)
            zyzNums.value.push(element.zyz)
        });
    })
    option.value.xAxis[0].data = regions.value
    option.value.series[0].data = gaNums.value
    option.value.series[1].data = jjNums.value
    option.value.series[2].data = cgNums.value
    option.value.series[3].data = zyzNums.value
    option && myChart.setOption(option.value);
}
var option = ref({
    title: {
        text: '',
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data: ['公安', '交警', '城管', '志愿者']
        // data: ['供电量（单位：kWh）', '售电量（单位：kWh）', '耗损电量（单位：kWh）']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [
        {
            type: 'category',
            // '钱塘里', '长生里', '劝业里', '学士里', '龙翔里', '仁和里', '东坡里', '将军里', '泗水里', '标记一', '标记二', '标记三'
            data: []
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '公安',
            type: 'bar',
            data: []
        },
        {
            name: '交警',
            type: 'bar',
            data: []
        },
        {
            name: '城管',
            type: 'bar',
            data: []
        },
        {
            name: '志愿者',
            type: 'bar',
            data: []
        }
    ]
})
import axios from 'axios'
import { get } from "node_modules/axios/index.cjs";
interface peopleChart {
    value: number,
    itemStyle: {
        color: string
    }
}
interface NumberOfPeople {
    id: number,
    region: string,
    number: number,
    longitude: string,
    latitude: string,
    area: number,
    vicinity: string,
    status: string,
    systemName: string
}

</script>

<style scoped>
.box {
    width: 100%;
    height: 100%;
    background-color: #034267;
}
</style>