/**
 * websocket
 */
// const wsUrl = "ws:/192.168.130.124:8015/websocket/"; // websocket 默认连接地址
let websocket2: any; // 用于存储实例化后websocket

function connectWebSocket2(wsUrl: string) {
    // 判断当前浏览器是否支持WebSocket
    if ("WebSocket" in window) {
        console.log("当前浏览器 windows");
    } else if ("MozWebSocket" in window) {
        console.log("当前浏览器 windows");
    } else {
        console.log("当前浏览器 Not support websocket");
    }
    websocket2 = new WebSocket(wsUrl);
}

export { connectWebSocket2, websocket2 };