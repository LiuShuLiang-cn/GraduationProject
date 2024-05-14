<template>
    <el-row class="deployment-wrapper">
        <el-col :span="24" class="deployment-box">
            <div class="field-group">
                <label for="deployment-number">部署人数:</label>
                <el-input-number v-model="peo" size="small" />
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
<script setup lang="ts">
import { ref, onMounted } from "vue";
import { ElCol, ElSpace, ElOption, ElSelect, ElButton, ElInputNumber } from "element-plus";
</script>
