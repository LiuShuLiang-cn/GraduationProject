<template>
    <el-row class="deployment-wrapper">
        <el-col :span="24" class="deployment-box">
            <div class="field-group">
                <label for="deployment-number">部署人数:</label>
                <el-input-number v-model="peoNum" size="small" />
            </div>
            <div class="field-group">
                <label for="deployment-location">部署地点:</label>
                <el-select v-model="addr" placeholder="请下拉选择" size="small">
                    <el-option v-for="(item, index) in addrs" :key="index" :label="item.label" :value="item" />
                </el-select>

            </div>
            <div class="button-container">
                <el-button type="primary" @click="BuShu" round size="small">部署</el-button>
            </div>
        </el-col>
    </el-row>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { ElCol, ElOption, ElSelect, ElButton, ElInputNumber, ElNotification } from "element-plus";
const peoNum = ref()
import axios from "axios";
const props = defineProps({
    systemId: String,
    role: String
})
interface ADDR {
    label: string,
    value: {
        lng: number,
        lat: number
    }
}
const addr = ref<ADDR>()
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
    }
])
import { useWebsocketStore } from "@/store/websocket";
const websocketStore = useWebsocketStore()

function BuShu() {
    const parm = addrs.value[0].value
    axios.get("/directives/deploy", {
        params: {
            role: props.role,
            lng: addr.value?.value.lng,
            lat: addr.value?.value.lat,
            systemId: props.systemId,
            num: peoNum.value
        }
    }).then((res: any) => {
        // TODO 部署成功之后在发送通知
        const old = websocketStore.deployList;
        // 查找匹配的元素索引
        const foundIndex = old.findIndex(item =>
            item.gaLat === addr.value?.value.lat && item.gaLng === addr.value?.value.lng
        );
        if (foundIndex !== -1) {
            if (props.role == '公安') {
                old[foundIndex].ga = peoNum.value;
            } else if (props.role == '交警') {
                old[foundIndex].jj = peoNum.value; console.log('部署了：', props.role)
            } else if (props.role == '城管') {
                old[foundIndex].cg = peoNum.value; console.log('部署了：', props.role)
            } else if (props.role == '志愿者') {
                old[foundIndex].zyz = peoNum.value; console.log('部署了：', props.role)
            }
        } else {
            console.log('未找到匹配的元素');
        }
        websocketStore.updateDeployList(old)
        ElNotification({
            title: '消息',
            message: '部署成功',
            type: 'success',
        })
    })
}
</script>
<style scoped>
.deployment-wrapper {
    padding: 0px;
    background-color: #ffffff;
    /* 使用纯白背景以突出阴影效果 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    /* 添加阴影以提升层次感 */
    border-radius: 15px;
    /* 圆角设定 */
    margin: 20px 0;
    /* 顶部和底部外边距 */

}

.field-group {
    margin-bottom: 20px;
    align-items: center;
    /* 垂直居中 */
    /* 字段组间距 */
    display: flex;
    justify-content: center;
    /* 新增：水平居中 */
    width: 100%;
    /* 确保field-group占满其父容器的宽度 */
}

.field-group label {
    flex-shrink: 0;
    margin-right: 10px;
}

.deployment-box {
    padding: 20px;
}

.el-select,
.el-input-number {
    width: 250px;
    /* 调整宽度为100%以填满容器 */
}

.button-container {
    display: flex;
    justify-content: center;
    /* 水平居中 */
    margin-top: 20px;
    /* 按钮与上面的间距 */
}
</style>