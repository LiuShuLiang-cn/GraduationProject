<template>
    <SeamlessScroll class="es-center-bottom" v-if="props.role == '指挥中心'">
        <div v-for="item, index in actions" class="es-bottom-item">
            <Title>{{ item.region }}</Title>
            <el-row class="item-container align-center" type="flex">
                <el-col :span="2">
                    <img class="role-icon" src="@/assets/images/screen/role/ga.svg" alt="My Icon">
                </el-col>
                <el-col :span="4">
                    <span style="font-size: 18px;">警力总人数：</span>
                </el-col>
                <el-col :span="18">
                    {{ item.ga_value + item.jj_value + item.zyz_value + item.cg_value }}
                </el-col>
            </el-row>
        </div>
    </SeamlessScroll>
    <control style="width: 100%;height: 100%;" v-if="role == '公安' || role == '交警' || role == '志愿者' || role == '城管'"
        :systemId="props.systemId" :role="props.role" />
    <control2 v-if="props.role == '市民'" style="width: 100%;height: 100%;" :systemId="props.systemId"
        :role="props.role" />
    <control3 v-if="props.role == '主办单位'" style="width: 100%;height: 100%;" :systemId="props.systemId"
        :role="props.role" />
    <control4 v-if="props.role == '公交地铁'" style="width: 100%;height: 100%;" :systemId="props.systemId"
        :role="props.role" />
</template>
<script setup lang='ts'>
import { ref, onMounted, computed, defineProps } from 'vue'
import control from "@/components/control/index-01.vue";
import control2 from "@/components/control/index-02.vue";
import control3 from "@/components/control/index-03.vue";
import control4 from "@/components/control/index-04.vue";
import Title from '../Title-diy.vue'
import 'odometer/themes/odometer-theme-default.css'
import SeamlessScroll from '@/components/SeamlessScroll.vue'
import { useWebsocketStore } from '@/store/websocket'
type Coordinates = string; // 使用字符串"纬度,经度"作为键
const props = defineProps({
    systemId: String,
    role: String
})

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
const actions = ref([
    { color: 'rgb(24, 144, 255)', icon: 'KnifeFork', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: "钱塘里" },
    { color: 'rgb(255, 192, 105)', icon: 'IceTea', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '长生里' },
    { color: 'rgb(92, 219, 211)', icon: 'Coffee', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '劝业里' },
    { color: 'rgb(179, 127, 235)', icon: 'IceCream', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '学士里' },
    { color: 'rgb(255, 133, 192)', icon: 'Dessert', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '龙翔里' },
    { color: 'rgb(255, 214, 102)', icon: 'GobletFull', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '仁和里' },
    { color: 'rgb(24, 144, 255)', icon: 'KnifeFork', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '东坡里' },
    { color: 'rgb(255, 192, 105)', icon: 'IceTea', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '将军里' },
    { color: 'rgb(92, 219, 211)', icon: 'Coffee', ga_value: 0, cg_value: 0, jj_value: 0, zyz_value: 0, region: '泗水里' },

])
const websocketStore = useWebsocketStore();
const deploys = computed(() => {
    return websocketStore.deployList;
})
onMounted(() => {
    actions.value = actions.value.map(action => {
        const matchingDeploy = deploys.value.find(deploy => getRegionNameByCoordinates(deploy.cgLng, deploy.cgLat) === action.region);
        if (matchingDeploy) {
            return {
                color: action.color || 'defaultColor',
                icon: action.icon || 'defaultIcon',
                ga_value: matchingDeploy.ga,
                cg_value: matchingDeploy.cg,
                jj_value: matchingDeploy.jj,
                zyz_value: matchingDeploy.zyz,
                region: action.region
            };
        }
        return action;
    });
})
</script>

<style lang='scss' scoped>
.es-center-bottom {
    position: relative;
    width: 100%;
    overflow: hidden;
    height: 150px;

    .es-bottom-item {
        position: absolute;
        top: 0;
        left: 0;
        width: 170px;
        height: 150px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        background-color: var(--es-block-bg);
        font-size: 22px;
        font-weight: 600;

        .es-item-text {
            margin-top: 16px;
        }
    }
}

.item-container {
    width: 100%;
}

.role-icon {
    width: 20px;
    /* 根据需要调整大小 */
    height: auto;
}

/* 调整元素对齐方式 */
.align-center {
    align-items: center;
}
</style>
