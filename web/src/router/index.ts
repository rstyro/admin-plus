/**
 * @file vue-router 路由配置
 * @description 静态路由 + 动态路由注入 + 全局守卫
 *   动态路由来自后端菜单，解析规则见 ./menuRoutes.ts，注入到名为 Layout 的主框架路由下
 */
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'
import { usePermissionStore } from '@/store/permission'
import { useTabsStore } from '@/store/tabs'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({ showSpinner: false })

/**
 * 静态路由：登录、错误页、主框架与固定页面
 * @description icon / demo/tinymce 在旧版是外壳里的固定入口（不在菜单表中），此处同样静态注册
 */
export const staticRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/welcome',
    children: [
      {
        path: 'welcome',
        name: 'Welcome',
        component: () => import('@/views/welcome/index.vue'),
        meta: { title: '欢迎页', affix: true, icon: 'fa fa-dashboard' },
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'fa fa-user' },
      },
      {
        path: 'icon',
        name: 'IconList',
        component: () => import('@/views/icon/index.vue'),
        meta: { title: 'ICON', affix: true, icon: 'fa fa-picture-o' },
      },
      {
        path: 'demo/tinymce',
        name: 'TinymceDemo',
        component: () => import('@/views/tinymce/demo.vue'),
        meta: { title: '富文本演示', icon: 'fa fa-pencil' },
      },
    ],
  },
  {
    path: '/redirect/:path(.*)',
    name: 'Redirect',
    component: () => import('@/views/redirect/index.vue'),
    meta: { title: '刷新中' },
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '页面不存在' },
  },
  {
    path: '/500',
    name: 'ServerError',
    component: () => import('@/views/error/500.vue'),
    meta: { title: '服务器异常' },
  },
  // 兜底跳 404
  { path: '/:pathMatch(.*)*', redirect: '/404' },
]

const router = createRouter({
  history: createWebHistory(),
  routes: staticRoutes,
})

/** 已注入的动态路由移除函数，登出时逐个调用 */
let dynamicRemovers: Array<() => void> = []

/**
 * 全局前置守卫
 * @description 登录态校验 + 认证信息与动态路由加载 + Tab 自动添加
 */
const WHITE_LIST = ['/login', '/404', '/500']

router.beforeEach(async (to, _from, next) => {
  NProgress.start()
  const userStore = useUserStore()
  const permissionStore = usePermissionStore()

  // 动态路由注册前，未匹配的地址会被兜底路由重定向到 /404；
  // 刷新页面时要以原始地址为准，注册完成后再导航回去
  const target = !permissionStore.isRoutesReady && to.redirectedFrom ? to.redirectedFrom : to

  // 已登录（cookie 里有 saToken 视为已登录）
  if (userStore.isLogin) {
    if (target.path === '/login') {
      next({ path: '/' })
      return
    }
    // 首次进入（或刷新后）拉取权限数据并生成动态路由
    if (!permissionStore.isRoutesReady) {
      try {
        await Promise.all([userStore.loadAuthInfo(), permissionStore.generateRoutes()])
        permissionStore.dynamicRoutes.forEach((r) => {
          // 与静态路由重名的菜单跳过注册，避免顶掉静态页面
          if (router.hasRoute(String(r.name))) return
          dynamicRemovers.push(router.addRoute('Layout', r))
        })
        // 路由表已变化，重新匹配一次目标路由
        next({ path: target.path, query: target.query, hash: target.hash, replace: true })
        return
      } catch {
        // 菜单/权限拉取失败（多为登录态失效）：清干净后回登录页
        resetRouter()
        await userStore.logout()
        next(`/login?redirect=${encodeURIComponent(target.fullPath)}`)
        return
      }
    }
  } else {
    // 未登录白名单放行
    if (!WHITE_LIST.includes(target.path)) {
      next(`/login?redirect=${encodeURIComponent(target.fullPath)}`)
      return
    }
  }
  next()
})

router.afterEach((to) => {
  // 只有主框架内的业务页才进 Tab
  if (to.matched.length > 1 && to.meta?.title) {
    useTabsStore().addTab(to)
  }
  // 同步浏览器标题，对应旧版每个页面 document.title 的设置逻辑
  document.title = to.meta?.title
    ? `${to.meta.title} - ${useUserStore().systemName || '后台管理系统'}`
    : useUserStore().systemName || '后台管理系统'
  NProgress.done()
})

/**
 * 重置路由（登出时调用）
 * @description 逐个移除动态路由；静态路由常驻不动，
 *   原先"按 path 移除根路由再重建"的写法对匿名路由无效，会导致动态路由残留
 */
export function resetRouter() {
  dynamicRemovers.forEach((remove) => remove())
  dynamicRemovers = []
  usePermissionStore().reset()
}

export default router
