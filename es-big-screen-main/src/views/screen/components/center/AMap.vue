<template>
    <div id="mapContainer"></div>
</template>

<script setup lang='ts'>
import { onMounted, shallowRef } from 'vue'
import { ElNotification, ElMessage } from 'element-plus'
import '@amap/amap-jsapi-types'
import AMapLoader from '@amap/amap-jsapi-loader'
import { useAmapStore } from '@/store/amap.ts'
import { Area } from "@/api/amap";
const map = shallowRef<AMap.Map | null>(null)
const mapStore = useAmapStore();
function initMap() {
    const options = {
        hideWithoutStyle: mapStore.hideWithoutStyle,
        areas: mapStore.areas
    }
    AMapLoader.load({
        key: '27c2dda621c33f1be9367a587b2e6e4c',
        version: '2.0',
        plugins: [],
    }).then((MyAMap: typeof AMap) => {
        var buildingLayer = new MyAMap.Buildings({ zIndex: 130, merge: false, sort: false, zooms: [17, 20] });
        map.value = new MyAMap.Map("mapContainer", {
            viewMode: '3D',
            zoom: 17,
            // zooms: [17, 20],
            pitch: 60,//初始地图俯仰角度
            rotation: 0,
            center: [120.1636, 30.251918],
            resizeEnable: true,
            dragEnable: true,
            zoomEnable: true,
            rotateEnable: true,
            showLabel: false, //不显示地图文字标记
            features: ['bg', 'point', 'road'],
            layers: [
                new AMap.TileLayer(),
                buildingLayer,
            ],
            scrollwheel: false,
            // 参考：https://lbs.amap.com/api/javascript-api-v2/guide/map/map-style
            mapStyle: 'amap://styles/light',
        });
        buildingLayer.setStyle(options); //此配色优先级高于自定义mapStyle
        addPolygon(options.areas, map.value)
        // 创建音乐喷泉标志
        const icon = new AMap.Icon({
            size: new AMap.Size(25, 34), //图标尺寸
            image: '//vdata.amap.com/icons/b18/1/2.png',
        });
        const marker = new MyAMap.Marker({
            // content: content, //自定义点标记覆盖物内容
            icon: icon,
            title: '西湖音乐喷泉',
            position: [120.161133, 30.253904], //基点位置
            offset: new AMap.Pixel(-13, -30), //相对于基点的偏移位置
        });
        marker.on('click', function (e) {
            ElMessage({
                type: 'info',
                message: '西湖音乐活动是否开启',
                plain: false,
            })
        })
        map.value.add(marker);
        // END
    }).catch(e => {
        console.log(e)
        ElMessage({
            message: '地图加载失败！',
            type: 'error',
            plain: true,
        })
    })
}

function addPolygon(areas: Area[], MyAMap: typeof AMap) {
    areas.forEach((element, index) => {
        const polygon = new AMap.Polygon({
            isOutline: true,
            outlineColor: "#ffeeff",
            borderWeight: 3,
            strokeDasharray: [20, 10], //虚线间隙的样式 strokeStyle 是 dashed 时有效, 20代表线段长度 10代表间隙长度
            bubble: true,
            lineJoin: "round",
            lineCap: "round",
            fillOpacity: 0,
            fillColor: '#123A51',
            strokeWeight: 6,
            strokeColor: "#2b8ae5",
            path: element.path,
            map: MyAMap
        })
    })
}

onMounted(() => {
    initMap()
})

defineExpose({
    map
})
</script>

<style lang='scss'>
#mapContainer {
    width: 100%;
    height: 100%;
}

.amap-logo,
.amap-copyright {
    opacity: 0;
}
</style>
