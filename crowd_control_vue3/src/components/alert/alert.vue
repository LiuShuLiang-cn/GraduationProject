<template>
    <div>
        <!-- 其他内容 -->
        <el-alert v-if="isAlertVisible" title="Error alert" type="error" @close="isAlertVisible = false">
            <!-- 错误信息内容 -->
        </el-alert>
        <!-- 触发错误提示的按钮或事件 -->
        <el-button @click="triggerErrorAlert">Trigger Error</el-button>
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { ElAlert, ElButton } from 'element-plus';


const isAlertVisible = ref(false); // 控制错误提示是否显示
const alertTitle = 'Error alert'; // 错误提示的标题

const triggerErrorAlert = () => {
    // 触发错误提示
    isAlertVisible.value = true;
    // 如果需要在一段时间后自动关闭提示，可以使用setTimeout
    setTimeout(() => {
        isAlertVisible.value = false;
    }, 3000);
};



</script>