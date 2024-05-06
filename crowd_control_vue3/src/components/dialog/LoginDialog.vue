<template>
    <el-dialog :model-value="props.IsShow" title="请选择" width="25%">
        <slot>
            系统：<el-select class="input" v-model="system" placeholder="Select" size="large" style="width: 240px">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select><br />
            角色：<el-select class="input" v-model="role" placeholder="Select" size="large" style="width: 240px">
                <el-option v-for="item in roles" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
        </slot>
        <template #footer>
            <span>
                <el-button type="primary" @click="login">
                    确定
                </el-button>
                <el-button type="primary" @click="emits('update:IsShow', false)">
                    取消
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup lang='ts'>
import { ref, defineEmits, onMounted } from 'vue'
import axios from 'axios';
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

async function login() {
    await axios.post("/user/login", {
        userName: props.name,
        password: props.pwd,
        role: role.value,
        num: 5,
        system: system.value
    }).then((res) => {
        if (res.data == '用户名错误！') {
            alert(res.data)
        } else {
            route.push({
                path: '/index',
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
