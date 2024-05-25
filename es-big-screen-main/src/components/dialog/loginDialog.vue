<template>
    <el-dialog :model-value="props.IsShow" title="请选择" width="30%">
        <!-- :before-close="handleClose" -->
        <!-- 使用div来包裹slot内容，并为其添加一些样式 -->
        <div class="dialog-content">
            <div class="select-group">
                <label>系统：</label>
                <el-select class="input" v-model="system" placeholder="Select" size="large">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
            </div>
            <div class="select-group">
                <label>角色：</label>
                <el-select class="input" v-model="role" placeholder="Select" size="large">
                    <el-option v-for="item in roles" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
            </div>
        </div>
        <!-- 使用footer slot自定义底部按钮区域 -->
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="login">
                    确定
                </el-button>
                <el-button @click="emits('update:IsShow', false)">
                    取消
                </el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped>
/* 给对话框内容增加一些内边距和排列 */
.dialog-content {
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

/* 设置选择器组合样式 */
.select-group {
    width: 100%;
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

/* 设置选择器标签样式 */
.select-group label {
    margin-right: 8px;
    white-space: nowrap;
    /* 避免标签折行 */
    width: 50px;
    /* 设置标签宽度 */
}

/* 设置选择器样式 */
.input {
    flex: 1;
    /* 选择器紧随标签占满剩余空间 */
}

/* 底部按钮样式，使它们并排显示并有间距 */
.dialog-footer {
    text-align: right;
}

.dialog-footer .el-button {
    margin-left: 10px;
}
</style>

<script setup lang='ts'>
import { ref, defineEmits, onMounted } from 'vue'
import axios from 'axios';
import { ElDialog, ElButton, ElSelect, ElOption } from "element-plus";
import { useRouter } from "vue-router";
const route = useRouter()
const props = defineProps(['IsShow', 'name', 'pwd'])
const system = ref('')
const options = ref([{
    value: 0,
    label: '创建新系统'
}])
const roles = ref([{
    value: '指挥中心', label: '指挥中心'
}, {
    value: '公安', label: '公安'
}, {
    value: '主办单位', label: '主办单位'
}, {
    value: '公交地铁', label: '公交地铁'
}, {
    value: '交警', label: '交警'
}, {
    value: '城管', label: '城管'
}, {
    value: '志愿者', label: '志愿者'
}, {
    value: '市民', label: '市民'
}])
const role = ref('')
const emits = defineEmits(['update:IsShow'])
import { ElNotification, ElMessage } from 'element-plus'
async function login() {
    await axios.post("/user/login", {
        userName: props.name,
        password: props.pwd,
        role: role.value,
        num: 5,
        system: system.value
    }).then((res) => {
        if (res.data == '用户名错误！') {
            ElMessage(res.data)
        } else if (res.data == '已登陆') {
            ElMessage(res.data)
        } else if (res.data == '密码错误！') {
            ElMessage(res.data)
        } else if (res.data == '发送异常！') {
            ElMessage(res.data)
        } else {
            route.push({
                path: '/screen',
                query: {
                    userName: props.name,
                    role: role.value,
                    system: res.data
                }
            })

        }

    })
    console.log('登录：\n', '系统id为', system.value, '用户名为：', props.name);

};
onMounted(() => {
    /**
     * 在登录页面加载
     */
    axios.get("/system/list").then((res) => {
        for (var i = 0; i < res.data.length; i++) {
            options.value.push({
                value: res.data[i].id, label: res.data[i].systemName
            })
        }
    })
})

</script>
