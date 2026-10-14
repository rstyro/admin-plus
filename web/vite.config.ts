import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

/**
 * Vite 配置文件
 * @description 配置 Vue 插件、开发期代理（解决前后端分离跨域）、路径别名
 *   后端地址通过环境变量 VITE_BACKEND_URL 统一管理，避免多端口不一致导致 session 丢失
 */
// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  // 加载对应 mode 的 .env.[mode] 文件
  const env = loadEnv(mode, process.cwd())
  // 后端服务地址，统一从环境变量读取，所有 proxy 共用同一个地址
  const backendUrl = env.VITE_BACKEND_URL || 'http://localhost:8800'

  return {
    plugins: [vue()],
    resolve: {
      alias: {
        // 将 @ 指向 src 目录，方便模块导入
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      port: 5173,
      // 开发期通过 vite 代理转发到后端 Spring Boot，避免前后端跨域问题
      proxy: {
        // 业务接口默认走 /api 前缀，方便和静态资源区分
        '/api': {
          target: backendUrl,
          changeOrigin: true,
          // 去掉 /api 前缀，让后端路由保持原样（/api/login -> /login）
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
        // 验证码图片接口直接走 /captcha
        '/captcha': {
          target: backendUrl,
          changeOrigin: true,
        },
        // 上传图片展示接口
        '/show': {
          target: backendUrl,
          changeOrigin: true,
        },
        // 文件上传接口
        '/upload': {
          target: backendUrl,
          changeOrigin: true,
        },
      },
    },
  }
})
