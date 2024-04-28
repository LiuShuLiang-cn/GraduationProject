<template>
    <el-dialog :model-value="props.selectUserShow" :title="props.title" :width="props.width" :draggable="drag"
        :before-close="beforeClose">
        <slot>
            <div>在这里写组件 </div>
        </slot>
        <template #footer>
            <span>
                <el-button type="primary" @click="emit('confirm', false)">
                    确定
                </el-button>
                <el-button @click="emit('update:selectUserShow', false)">
                    取消
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup lang='ts'>
import { computed, ref } from 'vue'

const props = defineProps({
    // 控制组件的显示与隐藏
    selectUserShow: {
        type: Boolean,
        default: false,
        required: true
    },

    title: {
        type: String,
        default: ''
    },
    width: {
        type: String,
        default: '80%'
    },
    drag: {
        type: Boolean,
        default: true
    }
})
const emit = defineEmits(['confirm', 'update:selectUserShow'])
// 查询用户的关键词
let searchTxt = ref('')


// 查询用户
const searchPerson = () => {

}


// 关闭对话框之前
const beforeClose = (done: Function) => {
    emit('update:selectUserShow', false)
    done()
}
searchPerson()
// 导出搜索用户的方法 , 是为了防止一个页面多个查询用户共用一个组件导致已选中的打开无法显示已选中的情况出现 , 使用方法为在打开组件时调用这个方法
defineExpose({
    searchPerson
})
</script>
