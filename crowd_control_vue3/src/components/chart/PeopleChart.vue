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
    initChart1();
    intervalId = setInterval(() => {
        initChart1()
    }, 5000);

});
onUnmounted(() => {
    clearInterval(intervalId);
})
var option = ref({
    tooltip: {
        // 鼠标悬浮提示数据
        trigger: "axis",
        backgroundColor: "rgba(32, 33, 36,.7)",
        borderColor: "rgba(32, 33, 36,0.20)",
        borderWidth: 15,
        textStyle: {
            // 文字提示样式
            color: "#fff",
            fontSize: "12",
        },
        axisPointer: {
            // 坐标轴虚线
            type: "cross",
            label: {
                backgroundColor: "#6a7985",
            },
        },
    },
    grid: {
        // 控制图表的位置
        left: "5%",
        right: "5%",
        top: "18%",
        bottom: "5%",
        containLabel: true,
    },
    xAxis: {
        axisLabel: {
            // X轴线 标签修改
            textStyle: {
                color: "white", //坐标值得具体的颜色
                fontSize: "10",
            },
        },
        data: ["A", "B", "C", "D", "E", "F"],
    },
    yAxis: {
        axisLabel: {
            // y轴线 标签修改
            textStyle: {
                color: "white", //坐标值得具体的颜色
            },
        },
    },
    series: [
        {
            data: [],
            type: "bar",
            barWidth: "48%", //调整柱状图宽度
            itemStyle: {
                normal: {
                    /*--------设置柱形图圆角 [左上角，右上角，右下角，左下角]-------------*/
                    borderRadius: [12, 12, 0, 0],
                    /*--------设置柱形图渐变色 -------------*/
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        {
                            offset: 0,
                            color: "rgba(0,244,255,1)",
                        },
                        {
                            offset: 1,
                            color: "rgba(0,77,167,1)",
                        },
                    ]),
                },
            },
        },
    ],
});
import axios from 'axios'
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
async function initChart1() {
    var x: string[] = []
    var y: peopleChart[] = []
    var myChart = echarts.init(chartDom.value);
    await axios.get("/data/" + props.systemId).then((res) => {
        const peopleList: NumberOfPeople[] = res.data.numberOfPeopleList
        peopleList.forEach(element => {
            x.push(element.region)
            y.push({
                value: element.number,
                itemStyle: {
                    color: element.status
                }
            })
        });
    })
    option.value.xAxis.data = x
    option.value.series[0].data = y
    option && myChart.setOption(option.value);
}
</script>

<style scoped>
.box {
    width: 100%;
    height: 100%;
    background-color: #034267;
}
</style>