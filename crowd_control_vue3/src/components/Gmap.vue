<script setup lang="ts">
import { onMounted, onUnmounted, ref, toRefs, defineProps, computed } from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import axios from "axios";
import myDialog from "./myDialog.vue";

var map: any = null;
// 地图样式修改+路线
var options =
{
    hideWithoutStyle: false,//是否隐藏设定区域外的楼块
    areas: [{ //围栏1:钱塘里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.158727, 30.258187], [120.158824, 30.257825], [120.158894, 30.257686], [120.15919, 30.257242], [120.159535, 30.2567], [120.159792, 30.256838], [120.159928, 30.256865], [120.162604, 30.256971], [120.16212, 30.257677], [120.161692, 30.258265], [120.158727, 30.258187]],
    }, { //围栏2：长生里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.161691, 30.258263], [120.162321, 30.257394], [120.162608, 30.256964], [120.162739, 30.256673], [120.162797, 30.256506], [120.162844, 30.256068], [120.162868, 30.255538], [120.163513, 30.255564], [120.163983, 30.255533], [120.163845, 30.25834], [120.161691, 30.258263]],
    },
    { //围栏3：劝业里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.159535, 30.2567], [120.159723, 30.256449], [120.159991, 30.256021], [120.160426, 30.255399], [120.160962, 30.255449], [120.161767, 30.255497], [120.162291, 30.255515], [120.162871, 30.255542], [120.162847, 30.256083], [120.162813, 30.256387], [120.162796, 30.25651], [120.162754, 30.256639], [120.162606, 30.256974], [120.159939, 30.256873], [120.159864, 30.256855], [120.15979, 30.256843], [120.159656, 30.256778], [120.159535, 30.2567]],
    },
    { //围栏4：学士里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.160426, 30.255399], [120.160052, 30.255294], [120.160244, 30.254972], [120.160396, 30.254745], [120.160864, 30.254064], [120.160887, 30.253976], [120.160869, 30.253896], [120.160858, 30.253817], [120.160981, 30.253778], [120.161085, 30.253721], [120.161243, 30.253763], [120.161505, 30.253895], [120.162902, 30.253965], [120.162866, 30.255539], [120.16176, 30.255494], [120.160966, 30.255445], [120.160426, 30.255399]],

    },
    { //围栏5：龙翔里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.162869, 30.255537], [120.162866, 30.255539], [120.162903, 30.253962], [120.163085, 30.253999], [120.164052, 30.254045], [120.163991, 30.255534], [120.163513, 30.255565], [120.16286, 30.255538]],
    },
    { //围栏6：仁和里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.1615, 30.253893], [120.16118, 30.253717], [120.161472, 30.253302], [120.161691, 30.252994], [120.161953, 30.252569], [120.162424, 30.251797], [120.162447, 30.251745], [120.162467, 30.251711], [120.162502, 30.251579], [120.162521, 30.251466], [120.163016, 30.25156], [120.16298, 30.252329], [120.162983, 30.252754], [120.162936, 30.253663], [120.162901, 30.253971], [120.1615, 30.253893]],
    },
    { //围栏7：东坡里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.162902, 30.253965], [120.162941, 30.253674], [120.162989, 30.25275], [120.162981, 30.252332], [120.163013, 30.251545], [120.163013, 30.250886], [120.163, 30.250751], [120.162968, 30.250656], [120.163251, 30.250533], [120.163406, 30.250503], [120.164225, 30.250435], [120.164048, 30.254043], [120.163082, 30.254001], [120.162902, 30.253965]],
    },
    { //围栏8：将军里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.164217, 30.250328], [120.163954, 30.250289], [120.163336, 30.250252], [120.163182, 30.250263], [120.162963, 30.250237], [120.1622, 30.249894], [120.162034, 30.249862], [120.161757, 30.249711], [120.161416, 30.24944], [120.161112, 30.249084], [120.160823, 30.248566], [120.161534, 30.248557], [120.162205, 30.24857], [120.163088, 30.248591], [120.164266, 30.248602], [120.164207, 30.249507], [120.164217, 30.250328]],
    },
    { //围栏9：泗水里
        visible: false,//是否可见
        rejectTexture: true,//是否屏蔽自定义地图的纹理
        color1: '#123A51',//楼顶颜色
        color2: '#123A51',//楼面颜色
        path: [[120.164301, 30.250335], [120.164342, 30.248594], [120.166338, 30.248584], [120.166357, 30.250424], [120.164301, 30.250335]],
    },
    ]
};
// 路口警力
var lnglats = [
    [120.160988, 30.257241],// 钱塘里
    [120.163306, 30.256641],// 长生里
    [120.161679, 30.256041],// 劝业里
    [120.162178, 30.254551],// 学士里
    [120.163452, 30.254666],//龙翔里
    [120.162555, 30.252937],//仁和里
    [120.163585, 30.252126],//东坡里
    [120.162707, 30.249315],//将军里
    [120.165169, 30.249562],//泗水里
    [120.161503, 30.253908],//
    [120.160966, 30.254118],
    [120.161199, 30.253807]];
const limitedLngLats = computed(() => lnglats.slice(9, 13));

var markers: any = [];
//实例化信息窗体
var content = [
    "<div class='info'>西湖文化活动",
    "<img src='https://webapi.amap.com/images/sharp.png'></div>",
];
/**
 * 获取index组件传来的值
 */
const props = defineProps({
    systemId: {
        type: String,
        require: true,
        default: "0"
    },
    role: String
})



var infoWindow: any
import { ElNotification, ElMessage } from 'element-plus'
import { websocket, connectWebSocket } from "@/utils/websocket";
var Map = null
onMounted(() => {
    // 加载地图
    AMapLoader.load({
        key: "27c2dda621c33f1be9367a587b2e6e4c", // 申请好的Web端开发者Key，首次调用 load 时必填
        version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: ["AMap.Scale"], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
    })
        .then((AMap) => {
            Map = AMap
            var buildingLayer = new AMap.Buildings({ zIndex: 130, merge: false, sort: false, zooms: [17, 20] });
            // 创建地图  Start
            map = new AMap.Map("container", {
                viewMode: '3D',
                zoom: 17,
                // zooms: [17, 20],
                pitch: 60,
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
            // END
            buildingLayer.setStyle(options); //此配色优先级高于自定义mapStyle

            // 增加区域框线  Start
            options.areas.forEach((element, index) => {
                // 参考网页https://lbs.amap.com/api/javascript-api-v2/tutorails/add-polygon
                //多边形 Polygon对象添加到 Map
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
                    path: options.areas[index].path,
                    map: map
                })
                //鼠标移入更改样式
                polygon.on("mouseover", () => {
                    polygon.setOptions({
                        fillOpacity: 0.7, //多边形填充透明度
                        fillColor: "#7bccc4",
                        cursor: 'pointer',
                    });
                });
                //鼠标移出恢复样式
                polygon.on("mouseout", () => {
                    polygon.setOptions({
                        fillOpacity: 0.5,
                        fillColor: "#ffeeff",
                        cursor: 'ani',
                    });
                });
                map.add(polygon);
            })

            // 点击查看经纬度  Start
            map.on('click', function (e: any) {
                console.log('经纬度：', e.lnglat.getLng() + ',' + e.lnglat.getLat())
            });
            // END

            limitedLngLats.value.forEach((element, index) => {
                console.log(index, element)
                var text = new AMap.Marker({
                    title: '标志',
                    position: new AMap.LngLat(element[0], element[1]), //点标记在地图上显示的位置
                });
                // text.setText(index + 1)
                text.on('click', (function (lng, lat) {
                    return function () {
                        show(lng);
                    };
                })(element[0], element[1]));
                map.add(text)
            })
            // 创建覆盖物群组，并将 marker 传给 OverlayGroup
            var overlayGroups = new AMap.OverlayGroup(markers);
            // 添加覆盖物群组
            map.add(overlayGroups);
            // END

            // 创建音乐喷泉标志
            const icon = new AMap.Icon({
                size: new AMap.Size(25, 34), //图标尺寸
                image: '//vdata.amap.com/icons/b18/1/2.png',
            });
            const marker = new AMap.Marker({
                // content: content, //自定义点标记覆盖物内容
                icon: icon,
                title: '西湖音乐喷泉',
                position: [120.161133, 30.253904], //基点位置
                offset: new AMap.Pixel(-13, -30), //相对于基点的偏移位置
            });
            marker.on('click', activitySwitch)
            map.add(marker);
            // END

            // 信息窗体 Start
            infoWindow = new AMap.InfoWindow({
                isCustom: true, //使用自定义窗体
                content: content.join("<br>"),
                offset: new AMap.Pixel(16, -45),
            });

            // END

        })
        .catch((e) => {
            console.log(e);
            ElNotification({
                title: 'Error',
                message: "初始化地图报错，请稍后重新进入。",
                type: 'error'
            })
        });
    // LoadPeoNum(map)
    // 建立websocket数据传输
    connectWebSocket("ws:/10.67.151.225:8015/websocket/data/" + props.systemId + '/' + props.role)
    websocket.onopen = function (event: any) {
        ElMessage({ message: '数据传输通道建立', type: 'success' })
    }
    websocket.onmessage = function (event: any) {
        // const deployList = JSON.parse(event.data).deployList
        const numberOfPeopleList = JSON.parse(event.data).numberOfPeopleList
        updataMarkerText(numberOfPeopleList)
    }
}
);
function updataMarkerText(numberOfPeopleList: any) {
    numberOfPeopleList.forEach((element, index) => {
        var text = new Map.Text({
            text: '335', //标记显示的文本内容
            anchor: "center", //设置文本标记锚点位置
            draggable: false, //是否可拖拽
            cursor: "pointer", //指定鼠标悬停时的鼠标样式。
            angle: 0, //点标记的旋转角度
            style: {
                //设置文本样式，Object 同 css 样式表
                "padding": ".75rem 1.25rem",
                "margin-bottom": "1rem",
                "border-radius": ".25rem",
                "background-color": "white",
                "width": "3rem",
                "border-width": 0,
                "box-shadow": "0 2px 6px 0 rgba(114, 124, 245, .5)",
                "text-align": "center",
                "font-size": "15px",
                "color": "blue",
            },
            position: new Map.LngLat(element.longitude, element.latitude), //点标记在地图上显示的位置
        });
        text.on('click', (function (lng, lat) {
            return function () {
                show(lng);
            };
        })(element.longitude, element.latitude));
        text.setText(element.number)
        text.setMap(map);
    });

}
/**
 * 西湖文化活动窗体
 * @param {*} e 
 */
function activitySwitch(e: any) {
    console.log('Map:', map.getAllOverlays('marker'))
    if (infoWindow._style.visible) {
        infoWindow.open(map, [120.161133, 30.253904]);
        infoWindow._style.visible = false
    } else {
        infoWindow.close(map)
        infoWindow._style.visible = true
    }

}

const peopleNum = ref('')
const gaNum = ref('')
const jjNum = ref('')
const cgNum = ref('')
const zyzNum = ref('')
const isVisible = ref(false)
const region = ref('')

async function show(e: any) {
    console.log('click! lng=', e)
    await axios.get("/data/" + props.systemId).then((res) => {
        for (var i = 0; i < res.data.deployList.length; i++) {
            var element = res.data.deployList[i]
            console.log(element.gaLng, "==", e, element.galng == e)
            if (element.gaLng == e) {
                console.log(element.gaLng, "==", e, element.galng == e)
                jjNum.value = element.jj
                gaNum.value = element.ga
                cgNum.value = element.cg
                zyzNum.value = element.zyz
                console.log(i, ':', gaNum.value, '===', element.ga)
            }
        }
        for (var i = 0; i < res.data.numberOfPeopleList.length; i++) {
            var element = res.data.numberOfPeopleList[i]
            if (element.longitude == e) {
                peopleNum.value = element.number
                region.value = element.region
            }
        }
    })

    if (isVisible.value) {
        isVisible.value = false
    } else {
        isVisible.value = true
    }

}
onUnmounted(() => {
    map?.destroy();
});
</script>

<template>
    <div id="container"></div>
    <myDialog v-model:selectUserShow="isVisible" @confirm="show" title="详情">
        <el-text class="mx-1" type="success">
            地区名：{{ region }}<br></br>
        </el-text>
        <el-text class="mx-1" type="success">
            当前People人数为：{{ peopleNum }}<br></br>
        </el-text>
        <el-text class="mx-1" type="success" v-if="role == '公安' || role == '指挥中心'">
            当前公安人数为：{{ gaNum }}<br></br>
        </el-text>
        <el-text class="mx-1" type="success" v-if="role == '交警' || role == '指挥中心'">
            当前交警人数为：{{ jjNum }}<br></br>
        </el-text>
        <el-text class="mx-1" type="success" v-if="role == '城管' || role == '指挥中心'">
            当前城管人数为：{{ cgNum }}<br></br>
        </el-text>
        <el-text class="mx-1" type="success" v-if="role == '志愿者' || role == '指挥中心'">
            当前志愿者人数为：{{ zyzNum }}<br></br>
        </el-text>
    </myDialog>
</template>

<style>
#container {
    width: 100%;
    height: 800px;
}

/* 信息窗体 Start */
.info {
    background-color: #e0dada;
    text-align: center;
    padding: 10px;
    position: relative;
    border: 1px solid #b9b9b9;
}

.info img {
    width: 30px;
    height: 23px;
    position: absolute;
    left: calc(50% - 15px);
    bottom: -23px;
}

.info el-switch {
    --el-switch-on-color: #13ce66;
    --el-switch-off-color: #ff4949;
}

/* END */
</style>
