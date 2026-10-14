/**
 * @file 自定义指令统一导出
 * @description 在 main.ts 中遍历注册
 */
import type { Directive } from 'vue'
import { permission } from './permission'

export const directives: Record<string, Directive> = {
  permission,
}
