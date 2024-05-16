<template>
    <div id="mapContainer"></div>
</template>

<script setup lang='ts'>
import { onMounted, shallowRef, computed, watchEffect } from 'vue'
import { ElMessage } from 'element-plus'
import '@amap/amap-jsapi-types'
import AMapLoader from '@amap/amap-jsapi-loader'
import { useAmapStore } from '@/store/amap'
import { Area } from "@/api/amap";
import { useWebsocketStore } from '@/store/websocket'
const map = shallowRef<AMap.Map | null>(null)
const mapStore = useAmapStore();
const websocketStore = useWebsocketStore()

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
        // @ts-ignore
        var buildingLayer = new MyAMap.Buildings({ zIndex: 130, merge: false, sort: false, zooms: [17, 20] });
        map.value = new MyAMap.Map("mapContainer", {
            viewMode: '3D',
            zoom: 17,
            // zooms: [17, 20],
            pitch: 60,//初始地图俯仰角度
            rotation: 0,
            center: [120.1636, 30.251918],
            // @ts-ignore
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
        // @ts-ignore
        addPolygon(options.areas, map.value)
        // @ts-ignore
        // createMusicMark(map.value)
        // END

    }).catch(e => {
        ElMessage({
            message: '地图加载失败！',
            type: 'error',
            plain: true,
        })
    })
}

function addPolygon(areas: Area[], MyAMap: typeof AMap) {
    areas.forEach((element, index) => {
        // @ts-ignore
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
        });
        //鼠标移入更改样式
        // polygon.on("mouseover", () => {
        //     polygon.setOptions({
        //         fillOpacity: 0.7, //多边形填充透明度
        //         fillColor: "#7bccc4",
        //         cursor: 'pointer',
        //     });
        // });
        //鼠标移出恢复样式
        // polygon.on("mouseout", () => {
        //     polygon.setOptions({
        //         fillOpacity: 0.5,
        //         fillColor: "#ffeeff",
        //         cursor: 'ani',
        //     });
        // });
        // @ts-ignore
        MyAMap.add(polygon);
    })
}

function updataMarkerText(numberOfPeopleList: any, MyAMap: typeof AMap) {
    clearTexts()
    numberOfPeopleList.forEach((element: any) => {
        var text = new AMap.Text({
            // @ts-ignore
            map: MyAMap,
            text: '335', //标记显示的文本内容
            anchor: "center", //设置文本标记锚点位置
            draggable: false, //是否可拖拽
            cursor: "pointer", //指定鼠标悬停时的鼠标样式。
            angle: 0, //点标记的旋转角度
            style: {
                "padding": ".75rem 1.25rem",
                "margin-bottom": "1rem",
                "border-radius": ".25rem",
                "background-color": "transparent",
                "width": "10rem",
                "border-width": 0,
                "text-align": "center",
                "font-size": "20px",
                "color": element.status,
                "border": "none", // 设置无边框
                "box-shadow": "none", // 若不需要阴影，也可去掉
            },
            position: new AMap.LngLat(element.longitude, element.latitude), //点标记在地图上显示的位置
        });
        text.on('click', (function (lng, lat) {
            return function () {
                ElMessage(lat, lng)
            };
        })(element.longitude, element.latitude));
        text.setText(element.region + element.number)
        // @ts-ignore
        textsOnMap.push(text);
    });

}
var textsOnMap = []
// 清除地图上现有的所有 AMap.Text 对象
function clearTexts() {
    textsOnMap.forEach(function (text) {
        // @ts-ignore
        text.setMap(null);
    });
    textsOnMap = []; // 清空数组，移除了所有的引用
}
const numberOfPeopleList = computed(() => websocketStore.numberOfPeopleList);
onMounted(() => {
    initMap();
})
// 使用响应式引用而不是解构，以保持响应性
watchEffect(() => {
    if (map.value && numberOfPeopleList.value) {
        // @ts-ignore
        updataMarkerText(numberOfPeopleList.value, map.value);
    }
});
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
