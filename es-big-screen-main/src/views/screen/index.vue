<template>
    <div :class="['es-screen-container', store.theme]">
        <div ref="screenRef" class="es-screen">
            <Header />
            <div class="es-screen-main">
                <div class="es-screen-left">
                    <Right :systemId="systemId" :role="role" />
                </div>
                <div class="es-screen-center">
                    <Center :systemId="systemId" :role="role" />
                </div>
                <div class="es-screen-right">
                    <Left :systemId="systemId" :role="role" />
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
import { useRoute, useRouter } from 'vue-router'
import { onMounted } from 'vue'
const store = useScreenStore()
const { screenRef } = useResize()
import { websocket, connectWebSocket } from "@/utils/websocket";
import { useWebsocketStore } from '@/store/websocket'
const websocketStore = useWebsocketStore()
import { ElMessage } from 'element-plus'
import { DataResponse } from '@/api/websocket'
const route = useRoute()
import axios from "axios";
const systemId = route.query.system
const role = route.query.role
onMounted(() => {
    axios.get('/data/' + systemId).then((res) => {

    })
    //@ts-ignore
    initWebSocket(systemId, role);

})

function initWebSocket(systemId: string, role: string) {
    connectWebSocket("ws:/127.0.0.1:8015/websocket/data/" + systemId + '/' + role)
    websocket.onopen = function (event: any) {
        console.log('数据传输通道建立', event)
    }
    websocket.onmessage = function (event: any) {
        const data: DataResponse = JSON.parse(event.data)
        const numberOfPeopleList = data.numberOfPeopleList
        websocketStore.updateDeployList(data.deployList)
        websocketStore.updateNumberOfPeopleList(numberOfPeopleList)
        websocketStore.updateOperate(data.operate)
    }
    // todo 有待完善，并未实现
    websocket.onclose = function (event: any) {
        axios.get('/user/logout', {
            params: {
                systemId: systemId,
                role: role
            }
        })
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
