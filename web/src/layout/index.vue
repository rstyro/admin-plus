<script setup lang="ts">
/**
 * @file 应用主框架
 * @description 由旧版 index.html 重构而来
 *   - 顶部 Navbar（系统名/用户菜单）
 *   - 多 Tab 工具栏（替代 iframe 多 Tab）
 *   - 左侧 Sidebar 菜单
 *   - 中间 AppMain 内容区（keep-alive 多 Tab）
 */
import { computed } from 'vue'
import Navbar from './components/Navbar.vue'
import TabBar from './components/TabBar.vue'
import Sidebar from './components/Sidebar.vue'
import AppMain from './components/AppMain.vue'
import { useThemeStore } from '@/store/theme'

const themeStore = useThemeStore()
/** 皮肤色值转内联 CSS 变量，作用于整个布局（子组件用 var() 消费） */
const themeStyle = computed(() => themeStore.themeVars)
</script>

<template>
  <div class="app-wrapper" :style="themeStyle">
    <!-- 侧栏 -->
    <aside class="sidebar-container" :style="{ background: 'var(--sidebar-bg)' }">
      <Sidebar />
    </aside>
    <!-- 主区域 -->
    <div class="main-container">
      <Navbar />
      <TabBar />
      <AppMain />
    </div>
  </div>
</template>

<style scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
  width: 100vw;
}

.sidebar-container {
  /* 宽度交由 Sidebar 自身控制（展开 220px / 折叠 64px），折叠时不留空隙 */
  overflow: hidden;
  flex-shrink: 0;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
</style>
