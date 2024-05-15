<template>
    <el-row class="deployment-wrapper">
        <el-col :span="24" class="deployment-box">
            <div class="field-group">
                <label for="deployment-location">是否开启活动：</label>
                <el-switch v-model="activateStatus" class="ml-2" inline-prompt active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'statusActivity')"
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949">
                </el-switch>
            </div>
        </el-col>
    </el-row>
</template>


<script setup lang="ts">
import { ref, defineProps, computed } from "vue";
import { ElSwitch, ElCol, ElSpace, ElOption, ElSelect, ElButton, ElInputNumber, ElNotification } from "element-plus";
import { useWebsocketStore } from '@/store/websocket'
const websocketStore = useWebsocketStore();
import axios from 'axios';
const activateStatus = computed(() => {
    return websocketStore.operate.statusActivity == 1 ? true : false
});
// console.log("初始活动是否开启：", websocketStore.operate.statusActivity, activateStatus.value)
const props = defineProps({
    systemId: String,
    role: String
})
function changeActivate(e: any, type: String) {
    console.log('事件发生了变化：', e, '类型为：', type)
    var status = e ? 1 : 0
    axios.get("/operate/" + props.systemId + "/" + type + "/" + status).then((res) => {
        if (res.data == 'switch') {
            const data = websocketStore.operate;
            data.activateStatus = status;
            console.log(data)
            websocketStore.updateOperate(data);
            if (websocketStore.operate.activateStatus == 1 ? true : false) {
                ElNotification({
                    title: "success",
                    message: "活动开启",
                    type: 'success'
                });
            } else {
                ElNotification({
                    title: "success",
                    message: "活动关闭",
                    type: 'success'
                });
            }

        }


    })
}
</script>

<style scoped>
.field-group {
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: fit-content;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.field-group label {
    margin-right: 10px;
    font-size: 20px;
    font-weight: bold;
}

.deployment-wrapper {
    padding: 10px;
    background-color: #1caa94;
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
</style>
