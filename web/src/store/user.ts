/**
 * @file 用户 Store
 * @description 管理登录态、用户信息、角色与按钮权限
 *   - token 由 sa-token 通过 cookie 下发，前端不存储 token，从 cookie 读
 *   - userInfo 来自 /login 与 /auth/info，持久化到 localStorage 避免刷新丢失
 *   - roles/permissions 每次会话由 /auth/info 拉取（不持久化，保证权限实时）
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  login as loginApi,
  logout as logoutApi,
  getAuthInfo as authInfoApi,
  type LoginDto,
  type UserInfo,
} from '@/api/auth'

/**
 * 从 cookie 读取 sa-token 的 token 值
 * @description 与 request.ts 中的逻辑一致，用于判断登录态
 */
function getTokenFromCookie(): string {
  if (typeof document === 'undefined') return ''
  const match = document.cookie.match(/(?:^|;\s*)saToken=([^;]+)/)
  return match ? decodeURIComponent(match[1]) : ''
}

export const useUserStore = defineStore(
  'user',
  () => {
    /** 当前用户信息 */
    const userInfo = ref<UserInfo | null>(null)
    /** 角色名称列表 */
    const roles = ref<string[]>([])
    /** 权限 KEY 列表（sys_menu.permit） */
    const permissions = ref<string[]>([])
    /** 系统名称 */
    const systemName = ref('')
    /** 认证信息是否已加载 */
    const authLoaded = ref(false)
    /**
     * cookie 版本号
     * @description cookie 不是响应式数据，登录/登出后自增一次，
     *   让下面依赖它的 computed 重新读取，否则 token 会被永久缓存
     */
    const cookieVersion = ref(0)

    /** token 值（从 cookie 实时读取，不持久化） */
    const token = computed(() => {
      void cookieVersion.value
      return getTokenFromCookie()
    })

    /** 是否已登录（cookie 有 saToken 视为已登录） */
    const isLogin = computed(() => !!token.value)

    /**
     * 登录
     * @description 后端 login 返回 R<SysUser>，data 是用户信息
     *   token 不在 body 里，由 sa-token 通过 Set-Cookie 下发到浏览器
     */
    async function login(dto: LoginDto) {
      const res = await loginApi(dto)
      userInfo.value = res.data
      cookieVersion.value += 1
    }

    /**
     * 拉取当前用户的角色与权限（路由守卫在进入业务页前调用）
     */
    async function loadAuthInfo() {
      const res = await authInfoApi()
      const info = res.data
      if (info?.user) userInfo.value = info.user
      roles.value = info?.roles || []
      permissions.value = info?.permissions || []
      systemName.value = info?.systemName || ''
      authLoaded.value = true
    }

    /** 清理本地登录态 */
    function clear() {
      userInfo.value = null
      roles.value = []
      permissions.value = []
      systemName.value = ''
      authLoaded.value = false
      // 主动清理 cookie（避免残留）
      if (typeof document !== 'undefined') {
        document.cookie = 'saToken=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/'
      }
      cookieVersion.value += 1
    }

    /**
     * 登出：调用后端接口 + 清理本地状态
     * @description 后端 StpUtil.logout() 会让 sa-token cookie 失效
     */
    async function logout() {
      try {
        await logoutApi()
      } catch {
        // 忽略登出接口异常
      } finally {
        clear()
      }
    }

    /**
     * 判断是否拥有某个权限
     * @param perm 权限标识，如 'system:user:list:add'
     * @description 超级管理员（userId=1）后端会返回全部权限 KEY，因此无需在前端做 admin 通配
     */
    function hasPermission(perm: string): boolean {
      if (!userInfo.value) return false
      return permissions.value.includes(perm)
    }

    /**
     * 判断是否拥有任一权限（对应旧版 sa:hasPermissionOr）
     */
    function hasAnyPermission(...perms: string[]): boolean {
      if (!perms.length) return true
      return perms.some((p) => hasPermission(p))
    }

    return {
      token,
      isLogin,
      userInfo,
      roles,
      permissions,
      systemName,
      authLoaded,
      login,
      loadAuthInfo,
      logout,
      clear,
      hasPermission,
      hasAnyPermission,
    }
  },
  {
    // 持久化 userInfo，避免刷新页面后丢失（token 从 cookie 实时读）
    persist: {
      key: 'admin-plus-user',
      pick: ['userInfo'],
    },
  },
)
