<script setup lang="ts">
/**
 * @file 侧栏菜单项（递归组件）
 * @description 支持任意层级：有子节点渲染 el-sub-menu，叶子按解析结果渲染路由项或禁用项
 */
import type { NavNode } from '@/store/permission'

defineOptions({ name: 'SidebarItem' })

defineProps<{ item: NavNode }>()
</script>

<template>
  <!-- 目录：有子菜单 -->
  <el-sub-menu v-if="item.children.length" :index="`dir-${item.id}`">
    <template #title>
      <i v-if="item.icon" :class="item.icon" class="menu-icon" />
      <span>{{ item.text }}</span>
    </template>
    <SidebarItem v-for="child in item.children" :key="child.id" :item="child" />
  </el-sub-menu>

  <!-- 叶子菜单：已解析出前端路由 -->
  <el-menu-item v-else-if="item.path" :index="item.path">
    <i v-if="item.icon" :class="item.icon" class="menu-icon" />
    <template #title>
      <span>{{ item.text }}</span>
    </template>
  </el-menu-item>

  <!-- 叶子菜单：新前端还没有对应页面，禁用避免跳到 404 -->
  <el-menu-item v-else disabled :index="`none-${item.id}`">
    <i v-if="item.icon" :class="item.icon" class="menu-icon" />
    <template #title>
      <span>{{ item.text }}</span>
    </template>
  </el-menu-item>
</template>

<style scoped>
.menu-icon {
  width: 24px;
  margin-right: 4px;
  font-size: 15px;
  text-align: center;
}
</style>
