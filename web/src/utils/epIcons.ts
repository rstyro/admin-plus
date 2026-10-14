/**
 * @file Element Plus 图标注册表
 * @description sys_menu.icon 存 EP 图标名（如 Setting），侧栏、菜单管理页与图标参考页共用这一份清单
 */
import type { Component } from 'vue'
import * as ElIcons from '@element-plus/icons-vue'

export const epIconRegistry = ElIcons as unknown as Record<string, Component>

/** 全部图标名，按字母序 */
export const epIconNames = Object.keys(epIconRegistry).sort()

/** 名称是否为有效的 EP 图标名（菜单表脏数据据此判断） */
export function isEpIconName(name?: string): boolean {
  return Boolean(name && Object.prototype.hasOwnProperty.call(epIconRegistry, name.trim()))
}
