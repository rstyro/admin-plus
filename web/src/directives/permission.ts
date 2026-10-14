/**
 * @file v-permission 自定义指令
 * @description 替代旧版 Thymeleaf 的 sa:hasPermissionOr="system:user:list:add,admin"
 *   用法：v-permission="'system:user:list:add'"
 *        v-permission="['system:user:list:add','system:user:list:view']"  // 任一命中即显示
 *   权限来源：userStore.permissions（由 /auth/info 返回的 sys_menu.permit 列表）
 */
import type { Directive, DirectiveBinding } from 'vue'
import { useUserStore } from '@/store/user'

/** 元素入 DOM 时的原始 display，恢复显示时用 */
const ORIGINAL_DISPLAY = Symbol('permission-original-display') as unknown as string

/**
 * 检查当前用户是否拥有指定权限（任一命中即视为有权限）
 */
function check(binding: DirectiveBinding): boolean {
  const value = binding.value
  if (!value) return true
  const perms: string[] = Array.isArray(value) ? value : [value]
  return useUserStore().hasAnyPermission(...perms)
}

/**
 * 按权限结果显示/隐藏元素
 * @description 用 display 而不是移除节点：权限数据可能晚于挂载到达，
 *   移除后无法恢复，updated 钩子也就失效了
 */
function apply(el: HTMLElement, binding: DirectiveBinding) {
  if (!(ORIGINAL_DISPLAY in el)) {
    ;(el as unknown as Record<string, string>)[ORIGINAL_DISPLAY] = el.style.display
  }
  el.style.display = check(binding)
    ? (el as unknown as Record<string, string>)[ORIGINAL_DISPLAY]
    : 'none'
}

export const permission: Directive = {
  mounted: apply,
  updated: apply,
}
