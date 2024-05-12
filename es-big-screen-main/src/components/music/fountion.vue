<template>
    <div class="es-block-001">
        <video-player :src="videoSrc" :options="playerOptions" :volume="1.0" />
    </div>
</template>

<script setup>

import { ref } from 'vue'
// 视频链接地址
// https://www.bilibili.com/video/BV13X4y1E7N9/?spm_id_from=..search-card.all.click
// https://snapany.com/zh/bilibili
const videoSrc = ref('https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/56/03/1209970356/1209970356-1-192.mp4?e=ig8euxZM2rNcNbNV7bdVhwdlhbdjhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1715529694&gen=playurlv2&os=upos&oi=1866713016&trid=0c49083cdbd24d79add0cb095aed6bf8O&mid=0&platform=html5&upsig=0686bb7ad4a19eec4a17717e3929f7f1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=1&orderid=0,3&buvid=&build=7330300&f=O_0_0&bw=206921&logo=80000000');
// 视频播放器配置
let playerOptions = ref({
    height: 200,
    width: document.documentElement.clientWidth, //播放器宽度
    playbackRates: [0.7, 1.0, 1.5, 2.0], // 播放速度
    autoplay: 'any', // 如果true,浏览器准备好时开始回放。
    muted: true, // 默认情况下将会消除任何音频。
    loop: true, // 导致视频一结束就重新开始。
    preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
    language: 'zh-CN',
    aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
    fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
    notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
    controls: true,
    controlBar: {
        timeDivider: true,
        durationDisplay: true,
        remainingTimeDisplay: false,
        fullscreenToggle: true // 全屏按钮
    }
})

</script>
<style scoped>
.es-block-001 {
    width: 100%;
    height: 210px;
}
</style>