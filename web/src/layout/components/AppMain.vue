<script setup lang="ts">
/**
 * @file 主内容区
 * @description 渲染当前路由组件，并用 keep-alive 维护多 Tab 状态
 *   include 取 tabsStore.cachedViews（组件名列表），
 *   因此各视图必须用 defineOptions 声明与路由 name 一致的组件名，否则缓存永不命中
 */
import { computed } from 'vue'
import { useTabsStore } from '@/store/tabs'

const tabsStore = useTabsStore()

/** keep-alive include：所有已打开 Tab 的组件 name */
const cachedViews = computed(() => tabsStore.cachedViews)
</script>

<template>
  <div class="app-main">
    <router-view v-slot="{ Component }">
      <keep-alive :include="cachedViews">
        <component :is="Component" />
      </keep-alive>
    </router-view>
  </div>
</template>

<style scoped>
.app-main {
  flex: 1;
  padding: 12px;
  background: #f0f2f5;
  overflow-y: auto;
}
</style>
