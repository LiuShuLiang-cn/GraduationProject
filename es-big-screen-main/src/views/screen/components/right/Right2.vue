<template>
    <div class="es-block">
        <Title>活动与公交信息</Title>
        <div style="width: 100%;height: 90%;">
            <el-tabs :tab-position="'top'" class="demo-tabs">
                <el-tab-pane label="status">
                    <div class="art-text" v-if="operate.statusActivity == '0'">活动尚未开始</div>
                    <fountion v-if="operate.statusActivity == '1'"></fountion>

                </el-tab-pane>
                <el-tab-pane label="Bus" class="vertical-fullscreen">
                    <div class="left-column">
                        <el-text class="m-1 text-box " type="success" size="large">
                            公交A:{{ operate.busA === '1' ? '开启' : '关闭' }}
                        </el-text><br>
                        <el-text class="m-1 text-box" type="success" size="large">
                            公交B:{{ operate.busB === '1' ? '开启' : '关闭' }}
                        </el-text>
                    </div>
                    <div class="right-column">
                        <el-text class="m-1 text-box" type="success" size="large">
                            公交C:{{ operate.busC === '1' ? '开启' : '关闭' }}
                        </el-text><br>
                        <el-text class=" m-1 text-box" type="success" size="large">
                            公交D:{{ operate.busD === '1' ? '开启' : '关闭' }}
                        </el-text>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="Subway" class="vertical-fullscreen">
                    <div class="left-column">
                        <el-text class="m-1 text-box" type="success" size="large">
                            地铁A:{{ operate.subwayA === '1' ? '开启' : '关闭' }}
                        </el-text><br>
                        <el-text class="m-1 text-box" type="success" size="large">
                            地铁B:{{ operate.subwayB === '1' ? '开启' : '关闭' }}
                        </el-text>
                    </div>
                    <div class="right-column">
                        <el-text class="m-1 text-box" type="success" size="large">
                            地铁C:{{ operate.subwayC === '1' ? '开启' : '关闭' }}
                        </el-text><br>
                        <el-text class="m-1 text-box" type="success" size="large">
                            地铁D:{{ operate.subwayD === '1' ? '开启' : '关闭' }}
                        </el-text>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup lang='ts'>
import { ref, computed, ComputedRef } from 'vue'
import { OperateInfo } from '@/api/websocket';
import Title from '../Title.vue'
import { ElTabPane, ElTabs, ElText } from 'element-plus'
import { useWebsocketStore } from '@/store/websocket';
import fountion from '@/components/music/fountion.vue'
const websocketStore = useWebsocketStore();
websocketStore.operate.statusActivity
const operate: ComputedRef<OperateInfo> = computed(() => {
    return websocketStore.operate;

})

</script>

<style lang='scss' scoped>
.es-block {
    width: 100%;
    height: 100%;
}

.demo-tabs {
    height: 100%;
}

.el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
    height: auto;
}

.el-tabs--right .el-tabs__content,
.el-tabs--left .el-tabs__content {
    height: 100%;
}

.art-text {
    /* 这里添加你需要的艺术字样式 */
    font-size: 30px;
    font-weight: bold;
    color: #ff0000;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
    text-transform: uppercase;
    text-align: center;
    margin-top: 20px;
}

.vertical-fullscreen {
    display: flex;
    justify-content: center;
    /* This will place the two columns at the far edges of the container */
    padding: 20px;
}

.left-column,
.right-column {
    display: flex;
    flex-direction: column;
    margin-right: 30px;
    /* This will distribute space evenly between the two el-text elements */
}

.text-box {

    border: 2px solid #42b983;
    /* Element UI 主题色边框 */
    border-radius: 8px;
    /* 圆角边框 */
    padding: 10px 20px;
    /* 内部填充 */
    margin: 10px 0;
    /* 防止文本相互紧贴 */
    background-color: #f0f9eb;
    /* 轻淡的背景色 */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    /* 轻微的阴影以创建层次感 */
    transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
    /* 平滑的过渡效果 */
}

.text-box:hover {
    transform: translateY(-5px);
    /* 鼠标悬停时轻微上移 */
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
    /* 提升阴影以获得更强的层次感 */
}
</style>
