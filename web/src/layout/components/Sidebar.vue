<script setup lang="ts">
/**
 * @file 侧栏菜单
 * @description 渲染 permissionStore.navTree（后端菜单树 + 前端路由解析结果）
 *   对应旧版 index.html 的 sidebar-menu：logo + 折叠按钮 + 多级菜单
 */
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePermissionStore } from '@/store/permission'
import { useUserStore } from '@/store/user'
import { useThemeStore } from '@/store/theme'
import SidebarItem from './SidebarItem.vue'
import { Fold, Expand } from '@element-plus/icons-vue'
import type { NavNode } from '@/store/permission'

const route = useRoute()
const router = useRouter()
const permissionStore = usePermissionStore()
const userStore = useUserStore()
const themeStore = useThemeStore()

/** 当前皮肤色值（el-menu 颜色是 JS props，无法走 CSS 变量，直接绑定） */
const skin = computed(() => themeStore.currentSkin)

/** 菜单折叠（对应旧版 sidebar-toggle） */
const collapse = ref(false)

/** 当前激活路径 */
const activeMenu = computed(() => route.path)

/** 系统名，取后端 admin.common.systemName，未加载时兜底 */
const title = computed(() => userStore.systemName || '后台管理系统')

/** 默认展开全部目录节点 */
const openedGroups = computed(() => collectGroups(permissionStore.navTree))

function collectGroups(nodes: NavNode[], acc: string[] = []): string[] {
  nodes.forEach((n) => {
    if (n.children.length) {
      acc.push(`dir-${n.id}`)
      collectGroups(n.children, acc)
    }
  })
  return acc
}

/**
 * 点击菜单跳转
 * @description index 只有叶子菜单才是前端路由（目录/无对应页面的菜单以 dir-、none- 前缀区分）
 */
function handleSelect(index: string) {
  if (index.startsWith('/')) router.push(index)
}
</script>

<template>
  <div class="sidebar" :class="{ 'is-collapse': collapse }">
    <div class="sidebar-logo" @click="router.push('/welcome')">
      <i class="fa fa-cube logo-icon" />
      <span v-show="!collapse" class="logo-text">{{ title }}</span>
    </div>
    <el-menu
      :default-active="activeMenu"
      :default-openeds="openedGroups"
      :collapse="collapse"
      class="sidebar-menu"
      :background-color="skin.sidebarBg"
      :text-color="skin.sidebarText"
      :active-text-color="skin.accent"
      @select="handleSelect"
    >
      <!-- 固定入口：对应旧版侧栏「主导航」下的欢迎页，不在菜单表中 -->
      <el-menu-item index="/welcome">
        <i class="fa fa-dashboard menu-icon" />
        <template #title>欢迎页</template>
      </el-menu-item>
      <SidebarItem v-for="item in permissionStore.navTree" :key="item.id" :item="item" />
    </el-menu>
    <div class="sidebar-footer">
      <el-button link class="collapse-btn" @click="collapse = !collapse">
        <el-icon><component :is="collapse ? Expand : Fold" /></el-icon>
        <span v-show="!collapse">收起菜单</span>
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  width: 220px;
  height: 100%;
  background: var(--sidebar-bg, #222d32);
  transition: width 0.28s;
  overflow: hidden;
}
.sidebar.is-collapse {
  width: 64px;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 50px;
  padding: 0 16px;
  color: var(--logo-text, #fff);
  background: var(--logo-bg, #367fa9);
  border-bottom: 1px solid var(--logo-border, transparent);
  cursor: pointer;
  flex-shrink: 0;
}
.logo-icon {
  font-size: 20px;
}
.logo-text {
  font-size: 15px;
  font-weight: bold;
  white-space: nowrap;
}

.sidebar-menu {
  flex: 1;
  border-right: none;
  overflow-y: auto;
  overflow-x: hidden;
}

.menu-icon {
  width: 24px;
  margin-right: 4px;
  font-size: 15px;
  text-align: center;
}
/* el-menu collapse 时宽度固定为 64，避免撑破侧栏 */
.sidebar-menu:not(.el-menu--collapse) {
  width: 100%;
}

.sidebar-footer {
  flex-shrink: 0;
  border-top: 1px solid var(--sidebar-border, #1a2226);
  padding: 6px 0;
  text-align: center;
}
.collapse-btn {
  color: var(--sidebar-text, #b8c7ce);
  width: 100%;
}

/* 皮肤化 el-menu：hover/active 背景与子菜单面板色（色值取自旧版 all-skins.min.css） */
.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background-color: var(--sidebar-hover-bg);
  color: var(--sidebar-hover-text);
}
.sidebar-menu :deep(.el-menu--inline) {
  background-color: var(--sidebar-treeview-bg);
}
.sidebar-menu :deep(.el-menu-item.is-active) {
  background-color: var(--sidebar-hover-bg);
  color: var(--accent);
  border-right: 3px solid var(--accent);
}
</style>
