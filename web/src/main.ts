/**
 * @file 应用入口
 * @description 装配 Vue、Element Plus、Pinia（含持久化）、vue-router、自定义指令
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// Font Awesome 4.7：后端菜单表存的图标是 fa fa-* class，与旧前端保持同一套图标
import '@/assets/fontawesome/css/font-awesome.min.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { directives } from './directives'
import './style.css'

const app = createApp(App)

// Pinia 状态管理（含持久化插件，避免刷新后 token 丢失）
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)

// vue-router
app.use(router)

// Element Plus
app.use(ElementPlus)
// 全局注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component as never)
}

// 注册自定义指令（v-permission 等）
for (const [name, directive] of Object.entries(directives)) {
  app.directive(name, directive)
}

app.mount('#app')
