<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <el-row type="flex">
                    <el-col :span="20" style="align-items: center;">人员流动模拟实验</el-col>
                    <el-col :span="4" class="right-align">
                        <currentTime></currentTime>
                        <div class="user-profile">
                            <el-avatar src="/public/喷泉.svg"></el-avatar>
                            <span class="avatar-span">{{ userName }}</span>
                            <el-button type="danger">退出</el-button>
                        </div>
                    </el-col>
                </el-row>
            </el-header>
            <el-container>
                <leftSider :systemId="systemId" :role="role" />
                <el-main>
                    <MapComponent :systemId="systemId" :role="role" />
                </el-main>
                <el-aside style="width: 25%; background-color: #E4E7ED;">
                    <rightSider :role="role" :systemId="systemId" />
                </el-aside>
            </el-container>
        </el-container>
    </div>
</template>

<script setup lang="ts" name="Index">
import { onMounted } from "vue";
import currentTime from "@/components/CurrentTime.vue"
import leftSider from '@/components/LeftSider.vue'
import rightSider from '@/components/RightSider.vue'
// 引入地图组件
import MapComponent from '@/components/Gmap.vue';
import { useRoute } from 'vue-router'

/**
 * 获取登录传来的用户名、登录系统id、角色
 */
const route = useRoute()
const userName = route.query.userName
const systemId = route.query.system
const role = route.query.role


onMounted(() => {
    console.log("index页面", systemId);

})

</script>

<style scoped>
.common-layout {
    height: 100%;
    margin: 0px;
    padding: 0px
}

.el-aside {
    overflow: hidden !important;
}

.right-align {
    padding-right: 20px;
    /* 在左边添加一些内边距 */
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

.el-row {
    justify-content: space-between;
    display: flex;
}

.avatar-span {
    padding-right: 20px;
    margin-left: 10px;
    font-size: 18px;
}

.user-profile {
    display: flex;
    /*增加此行来实现用户名垂直居中*/
    align-items: center;
}

.el-header {
    margin: 0px;
    background-color: #3eaf7c;
    /* 设置背景颜色 */
    line-height: 10vh;
    /* 设置行高，保持与 el-header 的高度相同，以实现文本垂直居中 */
    color: white;
    /* 设置文本颜色为白色 */
    font-size: 18px;
    /* 设置文本字体大小 */
    padding-left: 20px;
    /* 在左边添加一些内边距 */
    box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
    /* 添加一些阴影，让 el-header 具有一些深度效果 */
    height: 10vh;
    /* 设置 el-header 的高度 */
    width: 100%;
    /* 设置 el-header 的宽度 */
    margin: 0px;
}


.el-main {
    padding: 0%;
    background-color: antiquewhite;
    width: 100%;
    height: 100%;
}
</style>