/**
 * @file vue-router 路由配置
 * @description 静态路由 + 动态路由注入 + 全局守卫
 *   动态路由来自后端菜单，解析规则见 ./menuRoutes.ts，注入到名为 Layout 的主框架路由下
 */
import {
  createRouter,
  createWebHistory,
  type RouteLocationNormalized,
  type RouteRecordRaw,
} from 'vue-router'
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
        meta: { title: '欢迎页', affix: true, icon: 'Odometer' },
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User' },
      },
      {
        path: 'icon',
        name: 'IconList',
        component: () => import('@/views/icon/index.vue'),
        meta: { title: 'ICON', affix: true, icon: 'Picture' },
      },
      {
        path: 'demo/tinymce',
        name: 'TinymceDemo',
        component: () => import('@/views/tinymce/demo.vue'),
        meta: { title: '富文本演示', icon: 'EditPen' },
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

  // 动态路由注册前，未匹配的地址会被兜底路由重定向到 /404，/ 也会被重定向到 /welcome；
  // 沿重定向链回溯到最原始的用户请求地址（链根是没有 path 的 LocationAsRaw 空对象）
  const originalRoute: RouteLocationNormalized = (() => {
    let t: RouteLocationNormalized = to
    while (t.redirectedFrom?.path) t = t.redirectedFrom as RouteLocationNormalized
    return t
  })()
  const target = permissionStore.isRoutesReady ? to : originalRoute

  // 已登录（cookie 里有 saToken 视为已登录）
  if (userStore.isLogin) {
    // 已登录访问登录页回首页；to.path 判断防止 redirect 参数变化引起的反复重定向
    if (to.path === '/login' || target.path === '/login') {
      next({ path: '/', replace: true })
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
        next({ path: '/login', replace: true, query: { redirect: target.fullPath } })
        return
      }
    }
  } else {
    // 未登录：白名单（登录页/错误页）放行，其余跳登录页。
    // 注意：这里必须以 to.path 为准判断，不能拿 redirectedFrom 的原始地址做白名单——
    // 否则 '/login?redirect=/' 这类跳转会被解析回 /welcome，守卫每次都返回同一路由、
    // 永远不满足放行条件，触发 vue-router 的无限重定向保护（导航中止、页面白屏）。
    // 兜底路由把未知地址重定向到 /404 时，/404 只是中转页（有 redirectedFrom），
    // 不能按白名单放行；用户主动访问 /404 时 redirectedFrom 为空对象，正常展示
    const isTransit404 = to.path === '/404' && Boolean(to.redirectedFrom?.path)
    // 已经跳过一次登录页就放行：redirect 里带的是原始地址，若每次重算 query
    // （如带时间戳参数）会让 vue-router 的同位置判定失效，绕过无限重定向检测
    const alreadyGoingLogin = to.path === '/login' && Boolean(to.query.redirect)
    if (alreadyGoingLogin || (WHITE_LIST.includes(to.path) && !isTransit404)) {
      next()
      return
    }
    next({ path: '/login', replace: true, query: { redirect: target.fullPath } })
    return
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
