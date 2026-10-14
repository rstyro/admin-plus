/**
 * @file 主题皮肤 Store
 * @description 迁移旧版 AdminLTE skins（adminlte.skin.js）：
 *   - 12 种皮肤命名与旧版完全一致（skin-blue / skin-black ... 6 色 × 深色/浅色侧栏）
 *   - 色值取自旧版 all-skins.min.css
 *   - 选中皮肤持久化到 localStorage（对应旧版 store('skin', cls)），刷新后自动恢复
 *   - 默认 skin-blue，与旧版 body class 一致
 */
import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

/** 一种皮肤的颜色配置（对应旧版该 skin 下 navbar/logo/sidebar 的全部关键色） */
export interface SkinOption {
  /** 皮肤 id，与旧版 body class 同名，如 skin-blue-light */
  id: string
  /** 显示名 */
  label: string
  /** 是否浅色侧栏（旧版 -light 后缀） */
  light: boolean
  /** 顶栏背景（旧版 .main-header .navbar） */
  navbarBg: string
  /** 顶栏文字/图标颜色（旧版 navbar a 颜色；skin-black 为深色） */
  navbarText: string
  /** Logo/侧栏头部背景（旧版 .main-header .logo） */
  logoBg: string
  /** Logo 文字颜色 */
  logoText: string
  /** 主强调色，同时作为侧栏激活菜单的左边条颜色（旧版 navbar 色 / border-left-color） */
  accent: string
  /** 侧栏主体背景（深色 #222d32 / 浅色 #f9fafc） */
  sidebarBg: string
  /** 侧栏菜单文字颜色 */
  sidebarText: string
  /** 侧栏菜单 hover/active 背景 */
  sidebarHoverBg: string
  /** 侧栏 hover/active 文字颜色 */
  sidebarHoverText: string
  /** 侧栏子菜单（treeview）背景 */
  treeviewBg: string
  /** 侧栏头部下边框（skin-black 为 #d7d7d7，其余透明） */
  logoBorder: string
}

export const SKIN_OPTIONS: SkinOption[] = [
  {
    id: 'skin-blue',
    label: '蓝',
    light: false,
    navbarBg: '#3c8dbc',
    navbarText: '#ffffff',
    logoBg: '#367fa9',
    logoText: '#ffffff',
    accent: '#3c8dbc',
    sidebarBg: '#222d32',
    sidebarText: '#b8c7ce',
    sidebarHoverBg: '#1e282c',
    sidebarHoverText: '#ffffff',
    treeviewBg: '#2c3b41',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-black',
    label: '黑',
    light: false,
    navbarBg: '#ffffff',
    navbarText: '#333333',
    logoBg: '#222222',
    logoText: '#ffffff',
    accent: '#3c8dbc',
    sidebarBg: '#222d32',
    sidebarText: '#b8c7ce',
    sidebarHoverBg: '#1e282c',
    sidebarHoverText: '#ffffff',
    treeviewBg: '#2c3b41',
    logoBorder: '#d7d7d7',
  },
  {
    id: 'skin-red',
    label: '红',
    light: false,
    navbarBg: '#dd4b39',
    navbarText: '#ffffff',
    logoBg: '#d73925',
    logoText: '#ffffff',
    accent: '#dd4b39',
    sidebarBg: '#222d32',
    sidebarText: '#b8c7ce',
    sidebarHoverBg: '#1e282c',
    sidebarHoverText: '#ffffff',
    treeviewBg: '#2c3b41',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-yellow',
    label: '黄',
    light: false,
    navbarBg: '#f39c12',
    navbarText: '#ffffff',
    logoBg: '#e08e0b',
    logoText: '#ffffff',
    accent: '#f39c12',
    sidebarBg: '#222d32',
    sidebarText: '#b8c7ce',
    sidebarHoverBg: '#1e282c',
    sidebarHoverText: '#ffffff',
    treeviewBg: '#2c3b41',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-purple',
    label: '紫',
    light: false,
    navbarBg: '#605ca8',
    navbarText: '#ffffff',
    logoBg: '#555299',
    logoText: '#ffffff',
    accent: '#605ca8',
    sidebarBg: '#222d32',
    sidebarText: '#b8c7ce',
    sidebarHoverBg: '#1e282c',
    sidebarHoverText: '#ffffff',
    treeviewBg: '#2c3b41',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-green',
    label: '绿',
    light: false,
    navbarBg: '#00a65a',
    navbarText: '#ffffff',
    logoBg: '#008d4c',
    logoText: '#ffffff',
    accent: '#00a65a',
    sidebarBg: '#222d32',
    sidebarText: '#b8c7ce',
    sidebarHoverBg: '#1e282c',
    sidebarHoverText: '#ffffff',
    treeviewBg: '#2c3b41',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-blue-light',
    label: '浅蓝',
    light: true,
    navbarBg: '#3c8dbc',
    navbarText: '#ffffff',
    logoBg: '#3c8dbc',
    logoText: '#ffffff',
    accent: '#3c8dbc',
    sidebarBg: '#f9fafc',
    sidebarText: '#333333',
    sidebarHoverBg: '#f4f4f5',
    sidebarHoverText: '#000000',
    treeviewBg: '#f4f4f4',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-black-light',
    label: '浅黑',
    light: true,
    navbarBg: '#ffffff',
    navbarText: '#333333',
    logoBg: '#ffffff',
    logoText: '#333333',
    accent: '#3c8dbc',
    sidebarBg: '#f9fafc',
    sidebarText: '#333333',
    sidebarHoverBg: '#f4f4f5',
    sidebarHoverText: '#000000',
    treeviewBg: '#f4f4f4',
    logoBorder: '#d7d7d7',
  },
  {
    id: 'skin-red-light',
    label: '浅红',
    light: true,
    navbarBg: '#dd4b39',
    navbarText: '#ffffff',
    logoBg: '#dd4b39',
    logoText: '#ffffff',
    accent: '#dd4b39',
    sidebarBg: '#f9fafc',
    sidebarText: '#333333',
    sidebarHoverBg: '#f4f4f5',
    sidebarHoverText: '#000000',
    treeviewBg: '#f4f4f4',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-yellow-light',
    label: '浅黄',
    light: true,
    navbarBg: '#f39c12',
    navbarText: '#ffffff',
    logoBg: '#f39c12',
    logoText: '#ffffff',
    accent: '#f39c12',
    sidebarBg: '#f9fafc',
    sidebarText: '#333333',
    sidebarHoverBg: '#f4f4f5',
    sidebarHoverText: '#000000',
    treeviewBg: '#f4f4f4',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-purple-light',
    label: '浅紫',
    light: true,
    navbarBg: '#605ca8',
    navbarText: '#ffffff',
    logoBg: '#605ca8',
    logoText: '#ffffff',
    accent: '#605ca8',
    sidebarBg: '#f9fafc',
    sidebarText: '#333333',
    sidebarHoverBg: '#f4f4f5',
    sidebarHoverText: '#000000',
    treeviewBg: '#f4f4f4',
    logoBorder: 'transparent',
  },
  {
    id: 'skin-green-light',
    label: '浅绿',
    light: true,
    navbarBg: '#00a65a',
    navbarText: '#ffffff',
    logoBg: '#00a65a',
    logoText: '#ffffff',
    accent: '#00a65a',
    sidebarBg: '#f9fafc',
    sidebarText: '#333333',
    sidebarHoverBg: '#f4f4f5',
    sidebarHoverText: '#000000',
    treeviewBg: '#f4f4f4',
    logoBorder: 'transparent',
  },
]

export const useThemeStore = defineStore(
  'theme',
  () => {
    /** 当前皮肤 id，默认与旧版一致 */
    const skin = ref<string>('skin-blue')

    /** 当前皮肤配置（id 非法时回退默认皮肤） */
    const currentSkin = computed(
      () => SKIN_OPTIONS.find((s) => s.id === skin.value) ?? SKIN_OPTIONS[0],
    )

    /** 皮肤颜色映射为 CSS 变量，绑定到布局根节点，子组件通过 var(--xxx) 消费 */
    const themeVars = computed<Record<string, string>>(() => {
      const s = currentSkin.value
      return {
        '--navbar-bg': s.navbarBg,
        '--navbar-text': s.navbarText,
        '--logo-bg': s.logoBg,
        '--logo-text': s.logoText,
        '--logo-border': s.logoBorder,
        '--accent': s.accent,
        '--sidebar-bg': s.sidebarBg,
        '--sidebar-text': s.sidebarText,
        '--sidebar-hover-bg': s.sidebarHoverBg,
        '--sidebar-hover-text': s.sidebarHoverText,
        '--sidebar-treeview-bg': s.treeviewBg,
        // 侧栏分隔线：深色皮肤用更暗的底色，浅色皮肤用灰边框
        '--sidebar-border': s.light ? '#e5e5e5' : '#1a2226',
      }
    })

    /** 切换皮肤（对应旧版 change_skin：记录选择并立即生效） */
    function setSkin(id: string) {
      if (!SKIN_OPTIONS.some((s) => s.id === id)) return
      skin.value = id
    }

    return { skin, currentSkin, themeVars, setSkin }
  },
  {
    // 独立持久化 key：旧版前端在 8808 端口仍占用 localStorage 的 'skin' 键（AdminLTE body class 语义），避免互相污染
    persist: {
      key: 'admin-plus-theme',
      pick: ['skin'],
    },
  },
)
