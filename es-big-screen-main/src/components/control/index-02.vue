<template>
    <el-row class="deployment-wrapper">
        <el-col :span="24" class="deployment-box">
            <div class="field-group">
                <label for="deployment-number">转移人数:</label>
                <el-input-number v-model="peoNum" size="small" />
            </div>
            <div class="field-group">
                <el-select v-model="from" placeholder="请下拉选择" size="small">
                    <el-option v-for="(item, index) in addrs" :key="index" :label="item.label" :value="item" />
                </el-select>
                <label for="deployment-location">转移到</label>
                <el-select v-model="to" placeholder="请下拉选择" size="small">
                    <el-option v-for="(item, index) in addrs" :key="index" :label="item.label" :value="item" />
                </el-select>
            </div>
            <div class="button-container">
                <el-button type="primary" @click="ZhuanYi" round size="small">转移</el-button>
            </div>
        </el-col>
    </el-row>
</template>
<script setup lang="ts">
import { ref, onMounted, defineProps } from "vue";
import { ElCol, ElSpace, ElOption, ElSelect, ElButton, ElInputNumber, ElNotification } from "element-plus";
import axios from 'axios'
interface ADDR {
    label: string,
    value: {
        lng: number,
        lat: number
    }
}
const from = ref()
const to = ref()
const props = defineProps({
    systemId: String,
    role: String
})
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
const peoNum = ref()
function ZhuanYi() {
    axios.get("/directives/trans", {
        params: {
            systemId: props.systemId,
            from: from.value.label,
            to: to.value.label,
            number: peoNum.value
        }
    }).then((res) => {
        if (res.data == '输入的人数超出了转移的范围') {
            ElNotification({
                title: 'Warning',
                message: res.data,
                type: 'warning'
            })
        } else {
            ElNotification({
                title: 'Success',
                message: res.data,
                type: 'success'
            })
        }

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
