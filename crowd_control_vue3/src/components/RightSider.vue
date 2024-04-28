<script lang="ts" setup>
import { websocket, connectWebSocket } from '@/utils/websocket';
import { ref, onMounted, defineProps } from 'vue'
import { ElNotification, ElMessage } from 'element-plus'
import axios from 'axios';
// 消息
interface Chat {
    id: string,
    systemName: string,
    fromRole: string,
    toRole: string,
    text: string,
    type: string,
    statue: string,
    time: string
}

const activateStatus = ref()
const busA = ref()
const busB = ref()
const busC = ref()
const busD = ref()
const subwayA = ref()
const subwayB = ref()
const subwayC = ref()
const subwayD = ref()
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

const peo = ref()
const msg = ref('')
const msgTypes = ref([{ value: "1", label: "指令" }, { value: "2", label: "普通消息" }, { value: "3", label: "群发" }])
const msgType = ref()
const toRoles = ref()
const toRole = ref('')

const msgPage = ref<Chat[]>([])
const todos = ref<Chat[]>([]);
const todo = ref()

/**
 * 获取index组件传来的值
 */
const props = defineProps({
    role: String,
    systemId: String
})

/**
 * 
 * @param e 活动是否举办
 */
function changeActivate(e: any, type: String) {
    console.log('事件发生了变化：', e, '类型为：', type)
    var status = e ? 1 : 0
    var systemId = 21
    axios.get("/operate/" + systemId + "/" + type + "/" + status).then((res) => {
        if (res.data == 'switch')
            alert('修改成功')
    })
}

onMounted(() => {
    // 获取活动、公交、地铁状态
    axios.get("/operate/" + props.systemId).then((res) => {
        busA.value = res.data.busA == 1 ? true : false
        busB.value = res.data.busB == 1 ? true : false
        busC.value = res.data.busC == 1 ? true : false
        busD.value = res.data.busD == 1 ? true : false
        subwayA.value = res.data.subwayA == 1 ? true : false
        subwayB.value = res.data.subwayB == 1 ? true : false
        subwayC.value = res.data.subwayC == 1 ? true : false
        subwayD.value = res.data.subwayD == 1 ? true : false
        activateStatus.value = res.data.statusActivity == 1 ? true : false
    })
    axios.get("/command", {
        params: {
            systemId: props.systemId,
            roleTopic: props.role
        }
    }).then((res) => {
        if (res.data.length != 0) {
            ElNotification({
                title: 'Warning',
                message: '你有未完成的任务',
                type: 'warning',
            })
        }
        todos.value = res.data
    })

    var r = [
        { value: "指挥中心", label: "指挥中心" },
        { value: "公安", label: "公安" },
        { value: "交警", label: "交警" },
        { value: "城管", label: "城管" },
        { value: "志愿者", label: "志愿者" },
        { value: "市民", label: "市民" },
        { value: "公交地铁", label: "公交地铁" },
    ]
    toRoles.value = r.filter(function (element) {
        return element.label !== props.role;
    });
    // 建立websocket连接
    connectWebSocket("ws:/192.168.130.124:8015/websocket/" + props.systemId + '/' + props.role)
    websocket.onopen = function (event: any) {
        ElMessage({ message: '连接成功', type: 'success' })
        var msg: Chat = {
            id: String(props.systemId),
            fromRole: String(props.role),
            systemName: '',
            statue: '0',
            toRole: toRole.value,
            type: '4',
            text: props.role + '上线了',
            time: new Date().toString()
        }
        websocket.send(JSON.stringify(msg))
    }
    websocket.onmessage = function (event: any) {
        var msg: Chat = JSON.parse(event.data)
        msgPage.value.push(msg)
        if (msg.type == '1') {
            // 指令
            todos.value.push(msg)

            ElNotification({
                title: '消息',
                message: '收到一条来自指挥中心的指令',
                type: 'success',
            })
        } else if (msg.type == '4') {
            ElMessage({ message: msg.text, type: 'success' })
        }
    };
    websocket.onerror = function (event: any) {
        ElNotification({
            title: 'Error',
            message: 'websocket发生了错误，请重新登录！',
            type: 'error'
        })
    };

    websocket.onclose = function (event: any) {
        ElNotification({
            title: 'Error',
            message: 'websocket发生了错误，请重新登录！',
            type: 'error'
        })
    };

})

function BuShu() {
    console.log(addr.value)
    const parm = addrs.value[0].value
    axios.get("/directives/deploy", {
        params: {
            role: props.role,
            lng: addr.value?.value.lng,
            lat: addr.value?.value.lat,
            systemId: props.systemId,
            num: peo.value
        }
    }).then((res: any) => {
        ElNotification({
            title: '消息',
            message: '部署成功',
            type: 'success',
        })
    })
}
function sendMsg() {
    if (msg.value == null || msg.value == '') {
        ElNotification({
            title: 'Warning',
            message: '请添加发送的内容',
            type: 'warning',
        })
        return;
    }
    if (msgType.value == null) {
        msgType.value = 2
    }
    const chat: Chat = {
        id: String(props.systemId),
        fromRole: String(props.role),
        toRole: toRole.value,
        type: msgType.value,
        text: msg.value,
        systemName: '',
        statue: String(0),
        time: new Date().toString()
    }
    msgPage.value.push(chat)
    console.log(msgPage.value)
    websocket.send(JSON.stringify(chat))
    msg.value = ''
}

function todoSucc(res: any) {
    console.log(res)
    // 找到要删除的元素的索引
    const index = todos.value.findIndex(chat => chat.id === res[0]);
    // 确保索引是有效的
    if (index > -1) {
        // 使用splice方法删除该元素
        todos.value.splice(index, 1);
    }
    axios.get("/command/success/" + res[res.length - 1]).then((res) => {
        ElNotification({
            title: 'Success',
            message: res.data,
            type: 'success',
        })
        axios.get("/command", {
            params: {
                systemId: props.systemId,
                roleTopic: props.role
            }
        }).then((res) => {
            todos.value = res.data
        })
    })

}
</script>

<template>
    <!-- 主办单位的功能 -->
    <!-- <el-row v-if="role == '主办单位'">
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                活动名称：<el-text style="color: #409EFF;font-size: 18px;">西湖音乐节</el-text>
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                是否举办活动： <el-switch v-model="activateStatus" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'statusActivity')" />
            </div>
        </el-col>
    </el-row> -->

    <!-- 公交地铁的功能 -->
    <!-- <el-row v-if="role == '公交地铁'">
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                公交A：<el-switch v-model="busA" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'busA')" />
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                公交B：<el-switch v-model="busB" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'busB')" />
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                公交C：<el-switch v-model="busC" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'busC')" />
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                公交D：<el-switch v-model="busD" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'busD')" />
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                地铁A是否运行： <el-switch v-model="subwayA" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'subwayA')" />
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                地铁B是否运行： <el-switch v-model="subwayB" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'subwayB')" />
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                地铁C是否运行： <el-switch v-model="subwayC" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'subwayC')" />
            </div>
        </el-col>
        <el-col :span="12">
            <div class="grid-content ep-bg-purple-dark">
                地铁D是否运行： <el-switch v-model="subwayD" class="ml-2" inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" active-text="是" inactive-text="否"
                    size="large" @change="changeActivate($event, 'subwayD')" />
            </div>
        </el-col>
    </el-row> -->
    <!-- 公安、交警、城管、志愿者 -->
    <el-row>
        <el-col v-if="role == '公安' || role == '交警' || role == '志愿者' || role == '城管'">
            <el-space wrap size=default>
                部署人数： <el-input-number v-model="peo" size="large" />
                部署地点：<el-select v-model="addr" placeholder="请下拉选择" size="large" style="width: 240px">
                    <el-option v-for="(item, index) in addrs" :key="index" :label="item.label" :value="item" />
                </el-select>
                <el-button type="primary" @click="BuShu" round>部署</el-button></el-space>
        </el-col>
    </el-row>

    <!-- 指挥中心 v-if="role == '指挥中心'"-->

    <div class="scrollbar-container">
        <el-scrollbar height="200px">
            <!-- 如果需要标题，可以取消注释 -->
            <!-- <div class="title">消息记录</div> -->
            <p v-for="item in msgPage" class="scrollbar-demo-item">
                {{ item.fromRole }}: {{ item.text }}
            </p>
        </el-scrollbar>
    </div>
    <div class="input-group">
        <span class="input-label">请输入内容：</span>
        <el-input v-model="msg" style="width: 70%;" placeholder="Please input" clearable />
    </div>

    <!-- 选择框和按钮的组合 -->
    <div class="select-group">
        <el-select v-if="role == '指挥中心'" v-model="msgType" placeholder="请选择消息类型"
            style="width: 240px; margin-right: 10px;">
            <el-option v-for="item in msgTypes" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="toRole" placeholder="请选择要发送的对象" style="width: 240px; margin-right: 10px;">
            <el-option v-for="item in toRoles" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
    </div>
    <el-button type="primary" @click="sendMsg" style="margin-top: 20px;">发送消息</el-button>

    <div>
        <el-scrollbar height="400px">
            <!-- <p v-for="item in todos" :key="item.id">{{ item.text }}</p> -->
            <el-checkbox-group v-model="todo" @change="todoSucc">
                <el-checkbox v-for="item in todos" :key="item.id" :label="item.text" :value="item.id"
                    class="scrollbar-todo-item" />
            </el-checkbox-group>
        </el-scrollbar>
    </div>
</template>

<style scoped lang="scss">
// 消息记录标题
.title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
}

/* 滚动条容器样式 */
.scrollbar-container {
    border: 1px solid #e4e7ed;
    /* 添加边框 */
    border-radius: 4px;
    /* 添加圆角 */
    padding: 10px;
    /* 内边距 */
    margin-bottom: 20px;
    /* 与下方组件的间距 */
}

/* 滚动条样式 */
.el-scrollbar {
    background-color: #f9f9f9;
}

/* 滚动条内的内容样式 */
.scrollbar-demo-item {
    margin: 5px 0;
    /* 段落之间的间距 */
}

/* 输入框和选择框的统一样式 */
.el-input__inner,
.el-select .el-input__inner {
    border-radius: 4px;
    /* 统一圆角 */
    border: 1px solid #dcdfe6;
    /* 统一边框 */
    height: 40px;
    /* 统一高度 */
    font-size: 14px;
    /* 统一字体大小 */
}

/* 按钮的样式调整 */
.el-button--primary {
    background-color: #409eff;
    /* 主题色 */
    border-color: #409eff;
    /* 主题色 */
    border-radius: 4px;
    /* 圆角 */
    height: 40px;
    /* 统一高度 */
    font-size: 14px;
    /* 统一字体大小 */
    padding: 0 20px;
    /* 按钮的内边距 */
}

/* 选择框下的换行符移除 */
.el-select>.el-input__inner::after {
    content: none !important;
}

/* 统一输入框和选择框的样式 */
.input-group {
    display: flex;
    align-items: center;
    /* 垂直居中 */
    margin-bottom: 20px;
    /* 添加间距 */
}

.input-label {
    margin-right: 10px;
    /* 标签与输入框之间的间距 */
    white-space: nowrap;
}

/* 确保输入框和选择框拥有一致的外观 */
.el-input__inner,
.el-select .el-input__inner {
    height: 40px;
    /* 统一高度 */
    line-height: 40px;
    /* 行高与高度一致，实现垂直居中 */
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    font-size: 14px;
    padding: 0 10px;
    /* 一些内边距 */
}

/* 对齐选择框和按钮 */
.select-group {
    display: flex;
    align-items: center;
}

/* 移除select后面的多余空格 */
.el-select .el-input__inner::after {
    content: none;
}

.scrollbar-todo-item {
    display: flex;
    align-items: center;
    justify-content: left;
    height: 50px;
    margin: 10px;
    text-align: center;
    border-radius: 4px;
    background: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
}
</style>
