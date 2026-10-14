/**
 * @file axios 请求封装
 * @description 统一 baseURL、请求拦截器（注入 sa-token）、响应拦截器（错误处理）
 *   后端 sa-token 配置 token-name=saToken，所以请求头名也是 saToken
 *   token 值登录后由 sa-token 通过 Set-Cookie 下发到浏览器，前端从 cookie 读取
 */
import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import router from '@/router'

/**
 * 后端统一返回结构：{ code, msg, data }
 */
export interface ApiResult<T = unknown> {
  /** 业务状态码：200 表示成功 */
  code: number
  /** 提示消息 */
  msg: string
  /** 业务数据 */
  data: T
}

/**
 * 从 cookie 读取 sa-token 的 token 值
 * @description 后端 token-name 配置为 saToken，登录成功后浏览器会写入 cookie
 * @returns token 字符串，未找到则返回空串
 */
function getSaTokenFromCookie(): string {
  const match = document.cookie.match(/(?:^|;\s*)saToken=([^;]+)/)
  return match ? decodeURIComponent(match[1]) : ''
}

/**
 * 创建 axios 实例
 * @description baseURL 走 vite 代理的 /api 前缀，避免跨域
 */
const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 30 * 1000,
})

/**
 * 请求拦截器
 * @description 从 cookie 读取 sa-token 值，注入到请求头 saToken
 *   后端 sa-token 同时支持 cookie 和 header 读取，header 优先级更高
 */
service.interceptors.request.use(
  (config) => {
    const token = getSaTokenFromCookie()
    if (token) {
      // header 名必须与后端 sa-token.token-name 配置一致（这里都是 saToken）
      config.headers['saToken'] = token
    }
    return config
  },
  (error) => Promise.reject(error),
)

/**
 * 响应拦截器
 * @description 统一处理业务 code 与 HTTP 错误
 */
service.interceptors.response.use(
  (response: AxiosResponse<ApiResult>) => {
    // 未登录时后端 LoginIntercept 会 302 到 /toLogin 页面，XHR 跟随重定向后拿到的是 HTML
    const finalUrl = response.request?.responseURL || ''
    if (finalUrl.includes('/toLogin')) {
      handleUnauthorized()
      return Promise.reject(new Error('登录状态已失效'))
    }
    const res = response.data
    // 业务成功：直接返回解包后的业务数据（替代 AxiosResponse）
    if (res.code === 200) {
      return res as unknown as AxiosResponse
    }
    // 401: 未登录或登录已过期
    if (res.code === 401) {
      handleUnauthorized(res.msg)
      return Promise.reject(res)
    }
    // 其他业务错误
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(res)
  },
  (error) => {
    const status = error?.response?.status
    if (status === 401) {
      handleUnauthorized()
    } else if (status === 403) {
      ElMessage.error('没有访问权限')
    } else if (status === 500) {
      ElMessage.error('服务器异常，请稍后重试')
    } else {
      ElMessage.error(error?.message || '网络异常')
    }
    return Promise.reject(error)
  },
)

/**
 * 处理登录态失效：弹窗提示后清理并跳转登录页
 */
let isReloginShown = false
function handleUnauthorized(msg?: string) {
  if (isReloginShown) return
  isReloginShown = true
  ElMessageBox.confirm(msg || '登录状态已过期，请重新登录', '提示', {
    confirmButtonText: '重新登录',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
    })
    .finally(() => {
      isReloginShown = false
    })
}

/**
 * 统一对外暴露的请求方法
 * @description 泛型 T 表示业务数据类型；返回值已经解包出 data 字段
 */
export function request<T = unknown>(config: AxiosRequestConfig): Promise<ApiResult<T>> {
  return service(config) as unknown as Promise<ApiResult<T>>
}

export default service
