<template>
    <!-- 公交地铁的功能 -->
    <el-row v-if="role == '公交地铁'" class="transport-row">
        <!-- 公交的背景框 -->
        <el-col :span="12" class="transport-column">
            <div class="bus-container">
                <div class="bus-switch">
                    公交A：<el-switch v-model="busA" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'busA')" />
                </div>
                <div class="bus-switch">
                    公交B：<el-switch v-model="busB" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'busB')" />
                </div>
                <div class="bus-switch">
                    公交C：<el-switch v-model="busC" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'busC')" />
                </div>
                <div class="bus-switch">
                    公交D：<el-switch v-model="busD" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'busD')" />
                </div>
            </div>
        </el-col>
        <!-- 地铁的背景框 -->
        <el-col :span="12" class="transport-column">
            <div class="subway-container">
                <div class="subway-switch">
                    地铁A是否运行：<el-switch v-model="subwayA" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'subwayA')" />
                </div>
                <div class="subway-switch">
                    地铁B是否运行：<el-switch v-model="subwayB" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'subwayB')" />
                </div>
                <div class="subway-switch">
                    地铁C是否运行：<el-switch v-model="subwayC" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'subwayC')" />
                </div>
                <div class="subway-switch">
                    地铁D是否运行：<el-switch v-model="subwayD" inline-prompt active-text="是" inactive-text="否"
                        @change="changeActivate($event, 'subwayD')" />
                </div>
            </div>
        </el-col>
    </el-row>
</template>



<script setup lang="ts">
import { ref, defineProps, computed } from "vue"; import axios from 'axios';
import { ElSwitch, ElCol, ElSpace, ElOption, ElSelect, ElButton, ElInputNumber, ElNotification } from "element-plus";
import { useWebsocketStore } from '@/store/websocket'
const websocketStore = useWebsocketStore();
const props = defineProps({
    systemId: String,
    role: String
})
console.log('初始值：', websocketStore.operate.busB)
const busA = computed(() => {
    return websocketStore.operate.busA == 1 ? true : false
})
const busB = computed(() => {
    return websocketStore.operate.busB == 1 ? true : false
})
const busC = computed(() => {
    return websocketStore.operate.busC == 1 ? true : false
})
const busD = computed(() => {
    return websocketStore.operate.busD == 1 ? true : false
})
const subwayA = computed(() => {
    return websocketStore.operate.subwayA == 1 ? true : false
})
const subwayB = computed(() => {
    return websocketStore.operate.subwayB == 1 ? true : false
})
const subwayC = computed(() => {
    return websocketStore.operate.subwayC == 1 ? true : false
})
const subwayD = computed(() => {
    return websocketStore.operate.subwayD == 1 ? true : false
})
function changeActivate(e: any, type: string) {
    const msg = ref('')
    if (e)
        msg.value = type + '已经' + '开启';
    else
        msg.value = type + '已经' + '关闭';

    var status = e ? 1 : 0
    axios.get("/operate/" + props.systemId + "/" + type + "/" + status).then((res) => {
        if (res.data == 'switch') {
            const data = websocketStore.operate;
            if (type == 'subwayA') {
                data.subwayA = status;
            } else if (type == 'subwayB') {
                data.subwayB = status;
            } else if (type == 'subwayC') {
                data.subwayC = status;
            } else if (type == 'subwayD') {
                data.subwayD = status;
            } else if (type == 'busA') {
                data.busA = status;
            } else if (type == 'busB') {
                data.busB = status;
            } else if (type == 'busC') {
                data.busC = status;
            } else if (type == 'busD') {
                data.busD = status;
            }
            data.activateStatus = status;
            websocketStore.updateOperate(data);
            ElNotification({
                title: "通知",
                message: msg.value,
                type: 'info'
            });

        }
    })
}
</script>


<style scoped>
.transport-row {
    display: flex;
    /* 使用Flexbox布局 */
    justify-content: space-between;
    /* 使列之间的间距均匀 */
    margin-bottom: 20px;
}

.transport-column {
    width: calc(50% - 10px);
    /* 假设你想要10px的间隔 */
    margin-right: 10px;
    /* 左边距，除了最后一个元素 */
}

.transport-column:last-child {
    margin-right: 0;
    /* 最后一个元素不需要右边距 */
    margin-left: auto;
    /* 将地铁列推到右侧 */
}

.transport-column .bus-container,
.transport-column .subway-container {
    display: flex;
    /* 使用Flexbox布局 */
    flex-wrap: wrap;
    /* 允许项目换行 */
    justify-content: flex-start;
    /* 默认值，项目在容器的起始位置排列 */
    background: #f2f2f2;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin: 10px 0;
}

.bus-switch,
.subway-switch {
    width: calc(50% - 10px);
    /* 设置每个开关容器的宽度为一半减去间隔 */
    margin-right: 10px;
    /* 右边距，除了每行的最后一个元素 */
    margin-bottom: 20px;
    /* 垂直边距，根据需要调整 */
    box-sizing: border-box;
    /* 确保宽度包括padding和border */
}

/* 每行的最后一个开关不需要右边距 */
.bus-switch:nth-child(2n),
.subway-switch:nth-child(2n) {
    margin-right: 0;
}

/* 如果有需要，可以为每行的第一个开关设置左边距 */
.bus-switch:first-child,
.subway-switch:first-child {
    margin-left: 0;
}

.el-switch {
    --el-switch-on-color: #13ce66;
    --el-switch-off-color: #ff4949;
    margin-left: 12px;
}
</style>