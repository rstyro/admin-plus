/**
 * @file 菜单相关接口
 * @description 既包含侧栏菜单树接口（替代旧版 Thymeleaf ${tabs}），
 *   也包含菜单管理的 CRUD 接口（与后端 SysMenuController 对接）
 */
import { request } from '@/utils/request'
import type { PageResult } from './user'

/** 菜单类型：0=目录 1=菜单 2=按钮 */
export type MenuType = 0 | 1 | 2

/**
 * 侧栏菜单 VO：与后端 TabsVo 字段一一对应
 * @description 用于主框架侧栏渲染与动态路由生成
 */
export interface MenuVo {
  id: string
  /** 菜单文本 */
  text: string
  /** 图标 class，旧数据存的是 fa 图标名（如 fa fa-user） */
  icon?: string
  /** 旧版后端页面地址，如 system/user/page；目录节点为空串，前端用 resolveMenuRoute 解析 */
  url?: string
  /** 地址类型（后端 TabsVo.urlType，如 relative） */
  urlType?: string
  /** 页签是否可关闭（后端 TabsVo.close） */
  close?: boolean
  /** 打开方式（后端 TabsVo.targetType，默认 iframe-tab） */
  targetType?: string
  /** 子菜单 */
  children?: MenuVo[]
}

/** 菜单实体 */
export interface SysMenu {
  id?: number
  parentId?: number
  menuName: string
  menuUrl?: string
  menuType?: MenuType
  permit?: string
  sortNum?: number
  icon?: string
  createTime?: string
  /** 子菜单（树形） */
  child?: SysMenu[]
  /** 是否在新增菜单时自动添加按钮权限（后端 boolean 字段） */
  addBtn?: boolean
  /** 当前用户是否拥有该菜单权限（后端 /menu/tabList 用） */
  hasPermit?: boolean
}

/** 菜单-角色关联 VO（角色授权用，对应后端 RoleMenuTreeVo） */
export interface RoleMenuVo {
  id: number
  parentId?: number
  menuName: string
  child?: RoleMenuVo[]
  /** 该角色是否已拥有此菜单（后端回显勾选用，新增场景不返回） */
  hasRoleFlag?: boolean
}

/**
 * 分页查询菜单（列表）
 */
export function getMenuPage(pageNum: number, pageSize: number, data: { keyword?: string }) {
  return request<PageResult<SysMenu>>({
    url: '/system/menu/list',
    method: 'post',
    params: { pageNum, pageSize },
    data,
  })
}

/**
 * 获取角色对应的菜单树（用于角色授权弹窗）
 * @param roleId 角色 ID，0 表示查全部菜单树
 */
export function getRoleMenuList(roleId: number) {
  return request<RoleMenuVo[]>({
    url: '/system/menu/getRoleMenuList',
    method: 'get',
    params: { roleId },
  })
}

/** 新增菜单 */
export function addMenu(data: SysMenu) {
  return request({ url: '/system/menu/add', method: 'post', data })
}

/** 编辑菜单 */
export function editMenu(data: SysMenu) {
  return request({ url: '/system/menu/edit', method: 'post', data })
}

/** 删除菜单 */
export function delMenu(id: number) {
  return request({ url: '/system/menu/del', method: 'get', params: { id } })
}

/** 批量删除菜单 */
export function batchDelMenu(ids: number[]) {
  return request({ url: '/system/menu/batchDel', method: 'post', data: ids })
}

/**
 * 获取当前用户的 Tab 菜单树
 * @description 前端用此结果生成动态路由与侧栏菜单（替代旧版 Thymeleaf ${tabs}）
 */
export function getTabMenuList() {
  return request<MenuVo[]>({
    url: '/menu/tabList',
    method: 'get',
  })
}
