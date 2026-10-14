/**
 * @file 多 Tab Store
 * @description 替代旧版 iframe 多 Tab（app_iframe.js），改用 keep-alive SPA 方式
 *   tabs 负责页签展示，cachedViews 负责 keep-alive 缓存名单（两者在刷新页签时会有短暂差异）
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { RouteLocationNormalized } from 'vue-router'

/** 一个 Tab 项 */
export interface TabItem {
  /** 路由名，与视图组件 defineOptions 的 name 一致，用于 keep-alive include */
  name: string
  /** 路由路径 */
  path: string
  /** Tab 标题 */
  title: string
  /** 不可关闭（对应旧版页签 close:false） */
  affix?: boolean
}

export const useTabsStore = defineStore('tabs', () => {
  /** 当前已打开的 Tab 列表 */
  const tabs = ref<TabItem[]>([])
  /** 当前激活的 Tab 路径 */
  const activePath = ref<string>('')
  /** keep-alive 缓存的组件名列表 */
  const cachedViews = ref<string[]>([])

  /**
   * 添加一个 Tab（已存在则只激活）
   * @param route vue-router 路由对象
   */
  function addTab(route: RouteLocationNormalized) {
    const name = String(route.name || '')
    const path = route.path
    const exists = tabs.value.find((t) => t.path === path)
    if (exists) {
      activePath.value = path
      return
    }
    tabs.value.push({
      name,
      path,
      title: (route.meta?.title as string) || name,
      affix: Boolean(route.meta?.affix),
    })
    activePath.value = path
    if (name && route.meta?.noCache !== true) addCached(name)
  }

  /** 加入 keep-alive 缓存名单 */
  function addCached(name: string) {
    if (name && !cachedViews.value.includes(name)) cachedViews.value.push(name)
  }

  /** 移出 keep-alive 缓存名单（页签刷新时先移除，组件才会真正重建） */
  function removeCached(name: string) {
    cachedViews.value = cachedViews.value.filter((n) => n !== name)
  }

  /**
   * 关闭指定 Tab
   * @param path 要关闭的 Tab 路径
   * @returns 关闭后应激活的 Tab 路径（用于路由跳转）
   */
  function closeTab(path: string): string | null {
    const idx = tabs.value.findIndex((t) => t.path === path)
    if (idx === -1) return null
    const target = tabs.value[idx]
    if (target.affix) return null // 固定 Tab 不可关闭

    tabs.value.splice(idx, 1)
    removeCached(target.name)
    // 如果关闭的不是当前激活的，则无需切换
    if (activePath.value !== path) return null
    // 优先激活右侧，没有则左侧
    const next = tabs.value[idx] || tabs.value[idx - 1]
    activePath.value = next ? next.path : '/'
    return activePath.value
  }

  /**
   * 关闭其他 Tab
   * @param path 保留的 Tab 路径
   */
  function closeOtherTabs(path: string) {
    tabs.value = tabs.value.filter((t) => t.affix || t.path === path)
    syncCached()
    activePath.value = path
  }

  /**
   * 关闭全部 Tab（保留 affix）
   */
  function closeAllTabs(): string {
    tabs.value = tabs.value.filter((t) => t.affix)
    syncCached()
    activePath.value = tabs.value[0]?.path || '/'
    return activePath.value
  }

  /** 按当前页签列表重算缓存名单 */
  function syncCached() {
    const names = tabs.value.map((t) => t.name)
    cachedViews.value = cachedViews.value.filter((n) => names.includes(n))
  }

  /**
   * 重置（登出时调用）
   */
  function reset() {
    tabs.value = []
    activePath.value = ''
    cachedViews.value = []
  }

  return {
    tabs,
    activePath,
    cachedViews,
    addTab,
    addCached,
    removeCached,
    closeTab,
    closeOtherTabs,
    closeAllTabs,
    reset,
  }
})
