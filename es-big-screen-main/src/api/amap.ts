// 定义地图区域的接口
export interface Area {
    visible: boolean;
    rejectTexture: boolean;
    color1: string;
    color2: string;
    path: [number, number][];
}

// 定义 options 对象的接口
export interface MapOptions {
    hideWithoutStyle: boolean;
    areas: Area[];
}