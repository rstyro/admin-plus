/**
 * @file 菜单地址 → 前端路由解析
 * @description 数据库 sys_menu.menu_url 存的是旧版后端页面地址（如 system/user/page）。
 *   旧前端要保留对照，数据库与旧接口都不动，因此由前端把这层旧地址解析成
 *   vue-router 路由 + views 下的组件。
 *
 *   解析顺序：显式映射表 → 通用规则（去掉尾部 page 后按目录/文件名查找视图）。
 */
import type { Component } from 'vue'

/**
 * 视图组件注册表
 * @description 必须用 import.meta.glob 静态声明，构建期才能被分析；
 *   拼接的动态 import(`@/views/${x}/index.vue`) 在 Vite 下无法打包（运行即报 Unknown variable dynamic import）
 */
const viewModules = import.meta.glob<{ default: Component }>('../views/**/*.vue')

/** 解析结果 */
export interface ResolvedRoute {
  /** 前端路由 path */
  path: string
  /** 路由 name，同时是视图组件 defineOptions 的 name（keep-alive 依赖二者一致） */
  name: string
  /** 相对 src/views 的组件路径 */
  view: string
}

/** 旧菜单地址与新前端页面的对应关系（新前端目录结构与旧路径不一致的都在这里声明） */
const LEGACY_MENU_MAP: Record<string, ResolvedRoute> = {
  'system/user/page': { path: '/system/user', name: 'SystemUser', view: 'system/user/index.vue' },
  'system/role/page': { path: '/system/role', name: 'SystemRole', view: 'system/role/index.vue' },
  'system/menu/page': { path: '/system/menu', name: 'SystemMenu', view: 'system/menu/index.vue' },
  'system/btn/page': { path: '/system/btn', name: 'SystemBtn', view: 'system/btn/index.vue' },
  'system/sysOperLog/page': { path: '/log/operLog', name: 'OperLog', view: 'log/operLog.vue' },
  'system/sysLoginInfo/page': { path: '/log/loginInfo', name: 'LoginInfo', view: 'log/loginInfo.vue' },
  tinymceDemo: { path: '/demo/tinymce', name: 'TinymceDemo', view: 'tinymce/demo.vue' },
  icon: { path: '/icon', name: 'IconList', view: 'icon/index.vue' },
}

/** 由模块路径推导路由 name：system/user -> SystemUser */
function toRouteName(modulePath: string): string {
  return modulePath
    .split(/[-/]/)
    .filter(Boolean)
    .map((seg) => seg.charAt(0).toUpperCase() + seg.slice(1))
    .join('')
}

/**
 * 把后端菜单地址解析成前端路由
 * @param rawUrl sys_menu.menu_url 原值
 * @returns 解析结果；返回 null 表示该菜单在新前端没有对应页面（侧栏会禁用它，不会产生 404）
 */
export function resolveMenuRoute(rawUrl?: string | null): ResolvedRoute | null {
  const key = (rawUrl || '').trim().replace(/^\/+/, '').replace(/\/+$/, '')
  if (!key) return null

  const mapped = LEGACY_MENU_MAP[key]
  if (mapped) return mapped

  // 通用规则：兼容 xxx/page、xxx、a/b 等形态
  const modulePath = key.replace(/\/page$/, '')
  const view = [`${modulePath}/index.vue`, `${modulePath}.vue`].find((c) => viewModules[`../views/${c}`])
  if (!view) return null

  return {
    path: `/${modulePath}`,
    name: toRouteName(modulePath),
    view: view.replace('../views/', ''),
  }
}

/**
 * 取视图的懒加载函数
 * @param view 相对 src/views 的组件路径，如 system/user/index.vue
 */
export function loadView(view: string) {
  return viewModules[`../views/${view}`] as () => Promise<{ default: Component }>
}
