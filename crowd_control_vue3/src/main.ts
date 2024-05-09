import { createApp } from "vue";
// 引入element-plus/icons
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import locale from "element-plus/es/locale/lang/zh-cn"; // 因element-plus默认是英文，我们指定一下默认中文
// 图标并进行全局注册
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import router from "@/router";
import App from "./App.vue";
import axios from "axios";

axios.defaults.baseURL = 'http://127.0.0.1:8015'
axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded'

let app = createApp(App);


app.use(router)
    .use(
        ElementPlus, {
        locale,
        size: 'default' // 设置默认尺寸
    })

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}
app.mount("#app");
