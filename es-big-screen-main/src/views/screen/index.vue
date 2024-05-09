<template>
    <div :class="['es-screen-container', store.theme]">
        <div ref="screenRef" class="es-screen">
            <Header />
            <div class="es-screen-main">
                <div class="es-screen-left">
                    <Right />
                </div>
                <div class="es-screen-center">
                    <Center />
                </div>
                <div class="es-screen-right">
                    <Left />
                </div>
            </div>
        </div>
    </div>

</template>

<script setup lang='ts'>
import Header from './components/Header.vue'
import { useResize } from '@/utils/useResize'
import Left from './components/left/index.vue'
import Right from './components/right/index.vue'
import Center from './components/center/index.vue'
import { useScreenStore } from '@/store'
import { onMounted } from 'vue'
const store = useScreenStore()
const { screenRef } = useResize()
import { websocket, connectWebSocket } from "@/utils/websocket";
import { useWebsocketStore } from '@/store/websocket'
const websocketStore = useWebsocketStore()
import { ElMessage } from 'element-plus'
import { DataResponse } from '@/api/websocket'
onMounted(() => {
    initWebSocket()
})

function initWebSocket() {
    connectWebSocket("ws:/127.0.0.1:8015/websocket/data/" + '25' + '/' + '指挥中心')
    websocket.onopen = function (event: any) {
        ElMessage({ message: '数据传输通道建立', type: 'success' })
        console.log(event)
    }
    websocket.onmessage = function (event: any) {
        const data: DataResponse = JSON.parse(event.data)
        const numberOfPeopleList = data.numberOfPeopleList
        websocketStore.updateDeployList(data.deployList)
        websocketStore.updateNumberOfPeopleList(numberOfPeopleList)
        websocketStore.updateOperate(data.operate)

    }
}

</script>
<style lang='scss' scoped>
.es-screen-container {
    --es-screen-width: 1920px;
    --es-screen-height: 1080px;
    --es-header-height: 72px;
    --es-block-bg: #222733;
    --es-screen-bg: rgb(22, 21, 34);
    --es-screen-text-color: #fff;
    position: relative;
    margin: 0;
    width: 100%;
    height: 100%;
    background-size: 100% 100%;
    overflow: hidden;
    background: var(--es-screen-bg);
    color: var(--es-screen-text-color);
}

.es-screen {
    position: absolute;
    user-select: none;
    top: 50%;
    left: 50%;
    width: var(--es-screen-width);
    height: var(--es-screen-height);
    margin-left: calc(var(--es-screen-width) * 0.5 - var(--es-screen-width));
    margin-top: calc(var(--es-screen-height) * 0.5 - var(--es-screen-height));
    background-size: 100% 100%;
    padding: 0 20px;

    &-main {
        position: relative;
        display: flex;
        height: calc(100% - var(--es-header-height));
    }

    &-center {
        flex: 1;
        width: calc(var(--es-screen-width) - 1000px);
    }

    &-left,
    &-right {
        width: 460px;
    }
}

.light {
    --es-screen-bg: rgb(238, 238, 238);
    --es-block-bg: #fff;
    --es-screen-text-color: rgb(22, 21, 34);
}
</style>
