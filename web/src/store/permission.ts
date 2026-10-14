/**
 * @file 权限路由 Store
 * @description 取后端菜单树，产出两样东西：
 *   - navTree：侧栏渲染用的导航树（每个节点解析出前端路由 path）
 *   - dynamicRoutes：注入 vue-router 的动态路由
 *   菜单地址到组件的解析规则见 @/router/menuRoutes
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { getTabMenuList, type MenuVo } from '@/api/system/menu'
import { loadView, resolveMenuRoute } from '@/router/menuRoutes'

/** 侧栏导航节点 */
export interface NavNode {
  id: string
  text: string
  icon?: string
  /** 解析出的前端路由地址；null 表示新前端暂无对应页面 */
  path: string | null
  children: NavNode[]
}

export const usePermissionStore = defineStore('permission', () => {
  /** 侧栏导航树 */
  const navTree = ref<NavNode[]>([])
  /** 由菜单树生成的动态路由 */
  const dynamicRoutes = ref<RouteRecordRaw[]>([])
  /** 是否已生成过动态路由（避免重复生成） */
  const isRoutesReady = ref(false)

  /**
   * 拉取菜单并生成动态路由
   * @returns 需要注入 router 的路由记录
   */
  async function generateRoutes() {
    const res = await getTabMenuList()
    const routes: RouteRecordRaw[] = []
    const registered = new Set<string>()
    navTree.value = buildNavTree(res.data || [], routes, registered)
    dynamicRoutes.value = routes
    isRoutesReady.value = true
    return routes
  }

  /**
   * 递归构建导航树，同时收集可生成路由的叶子菜单
   * @description 后端 /menu/tabList 已过滤按钮节点，这里只会拿到目录与菜单
   */
  function buildNavTree(menus: MenuVo[], routes: RouteRecordRaw[], registered: Set<string>): NavNode[] {
    return menus.map((m) => {
      const children = m.children?.length ? buildNavTree(m.children, routes, registered) : []
      const resolved = resolveMenuRoute(m.url)
      // 同名路由只注册一次（多个菜单指向同一页面时共用）
      if (resolved && !registered.has(resolved.name)) {
        registered.add(resolved.name)
        routes.push({
          path: resolved.path,
          name: resolved.name,
          component: loadView(resolved.view),
          meta: { title: m.text, icon: m.icon },
        })
      }
      return {
        id: String(m.id),
        text: m.text,
        icon: m.icon,
        path: resolved?.path ?? null,
        children,
      }
    })
  }

  /** 重置（登出时调用） */
  function reset() {
    navTree.value = []
    dynamicRoutes.value = []
    isRoutesReady.value = false
  }

  return { navTree, dynamicRoutes, isRoutesReady, generateRoutes, reset }
})
