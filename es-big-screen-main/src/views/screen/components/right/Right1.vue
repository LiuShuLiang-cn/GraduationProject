<template>
    <div class="es-block">
        <Title>活动与公交信息</Title>
        <el-carousel motion-blur indicator-position="none">
            <el-carousel-item v-for=" item in 9" :key="item">
                <Chart :option="options[item]" style="width: 300px;" />
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script setup lang='ts'>
import { ref, computed } from 'vue'
import Title from '../Title.vue'
import Chart from '@/components/chart/Chart-div.vue'
import { ElCarousel, ElCarouselItem } from 'element-plus'
import { useWebsocketStore } from '@/store/websocket'
const webSocketStore = useWebsocketStore()
type Coordinates = string; // 使用字符串"纬度,经度"作为键
const locationMap: Map<Coordinates, string> = new Map([
    ["120.160988,30.257241", "钱塘里"],
    ["120.163306,30.256641", "长生里"],
    ["120.161679,30.256041", "劝业里"],
    ["120.162178,30.254551", "学士里"],
    ["120.163452,30.254666", "龙翔里"],
    ["120.162555,30.252937", "仁和里"],
    ["120.163585,30.252126", "东坡里"],
    ["120.162707,30.249315", "将军里"],
    ["120.165169,30.249562", "泗水里"],
]);

function getRegionNameByCoordinates(lat: number, lng: number): string | undefined {
    const key = `${lat.toFixed(6)},${lng.toFixed(6)}`; // 将经纬度格式化为字符串
    return locationMap.get(key);
}
const options = computed(() => {
    const deploys = webSocketStore.deployList
    let res = []
    deploys.forEach((element, index) => {
        const data = {
            grid: {
                containLabel: false,
            },
            legend: {
                bottom: '0%',
                icon: 'circle',
                data: ['公安', '交警', '城管', '志愿者'],
                textStyle: {
                    color: '#aaa'
                }
            },
            tooltip: {
                show: true,
                formatter: arg => {
                    const thirdCategory = arg.data;
                    let retStr = getRegionNameByCoordinates(deploys[index].cgLng, deploys[index].cgLat) + '地区的' + thirdCategory.name + ': ' + thirdCategory.value;
                    return retStr;
                }
            },
            series: [
                {
                    type: 'pie',
                    label: {
                        show: false
                    },
                    emphasis: {
                        label: {
                            show: true
                        },
                        labelLine: {
                            show: false
                        }
                    },
                    data: [{ name: '公安', value: element.cg }, { name: '交警', value: element.jj }, { name: '城管', value: element.cg }, { name: '志愿者', value: element.zyz }]
                }
            ]
        }
        // @ts-ignore
        res.push(data);
    })
    return res;
})

</script>

<style lang='scss' scoped>
.es-block {
    width: 100%;
    height: 100%;
}
</style>
