import axios from "axios";

// 创建一个axios实例
const request = axios.create({
    baseURL: "http://localhost:5173", //这里就是配置好的环境变量
    timeout: 2000,
});

// 添加请求拦截器
request.interceptors.request.use(
    (config) => {
        // 在发送请求之前做些什么
        return config;
    },
    (error) => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

// 添加响应拦截器
request.interceptors.response.use(
    async (response) => {
        const res = response.data;
        if (res.code == 666) {
            console.log("mock返回的数据");
            return res;
        } else if (res.code == 200) {
            console.log("后端服务器返回的数据");
            return res;
        }
    },
    (error) => {
        // 超出 2xx 范围的状态码都会触发该函数。
        // 对响应错误做点什么
        return Promise.reject(error);
    }
);

export { request };
