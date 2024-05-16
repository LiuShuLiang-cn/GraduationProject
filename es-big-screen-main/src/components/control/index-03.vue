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
import { defineProps, computed } from "vue";
import { ElSwitch, ElCol, ElNotification } from "element-plus";
import { useWebsocketStore } from '@/store/websocket'
import { websocket2 } from "@/utils/websocket-2";
const websocketStore = useWebsocketStore();
import axios from 'axios';
const activateStatus = computed(() => {
    return websocketStore.operate.statusActivity == '1' ? true : false
});
const props = defineProps({
    systemId: String,
    role: String
})
function changeActivate(e: any, type: String) {
    var status = e ? 1 : 0
    axios.get("/operate/" + props.systemId + "/" + type + "/" + status).then((res) => {
        if (res.data == 'switch') {
            const data = websocketStore.operate;
            data.statusActivity = String(status);
            websocketStore.updateOperate(data);

            let msg = ''
            if (websocketStore.operate.statusActivity == '1' ? true : false) {
                ElNotification({
                    title: "success",
                    message: "活动开启",
                    type: 'success'
                });
                msg = '音乐活动开启了，大家快来玩！';
            } else {
                ElNotification({
                    title: "success",
                    message: "活动关闭",
                    type: 'success'
                });
                msg = '音乐活动关闭了，请大家等待下次！';
            }
            const newMessage = {
                id: String(),
                fromRole: props.role,
                toRole: String(),
                type: '3',
                text: msg,
                systemName: props.systemId,
                statue: String(0),
                time: new Date().toString()
            };
            websocket2.send(JSON.stringify(newMessage))
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
