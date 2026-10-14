<script setup lang="ts">
/**
 * @file 菜单图标
 * @description 按名称渲染 Element Plus 图标（sys_menu.icon 存图标名，如 Setting）；
 *   名称未命中图标集时回退占位图标，避免脏数据渲染成空白
 */
import { computed, type Component } from 'vue'
import { epIconRegistry } from '@/utils/epIcons'

const registry = epIconRegistry

const props = withDefaults(
  defineProps<{
    /** Element Plus 图标名 */
    icon?: string
    /** 未命中时的占位图标名 */
    fallback?: string
  }>(),
  { icon: '', fallback: 'Menu' },
)

/** 无图标名时不渲染，与旧版 v-if="item.icon" 行为一致 */
const visible = computed(() => Boolean(props.icon?.trim()))

const resolved = computed<Component>(() => {
  const name = props.icon?.trim() || ''
  return registry[name] || registry[props.fallback] || registry.Menu
})
</script>

<template>
  <el-icon v-if="visible" class="menu-icon"><component :is="resolved" /></el-icon>
</template>

<style scoped>
.menu-icon {
  width: 24px;
  margin-right: 4px;
  font-size: 15px;
  text-align: center;
  vertical-align: middle;
}
</style>
