/**
 * @file 角色管理接口
 * @description 与后端 SysRoleController 对接
 */
import { request } from '@/utils/request'
import type { PageResult } from './user'

/** 角色实体 */
export interface SysRole {
  id?: number
  roleName: string
  remark?: string
  createTime?: string
  /** 角色-菜单绑定关系（保存时使用） */
  menuIdList?: number[]
}

/** 角色列表入参（含 keyword） */
export interface SysRoleDto {
  keyword?: string
}

/** 用户-角色 VO（授权弹窗用） */
export interface UserRoleVo {
  id: number
  roleName: string
  remark?: string
  /** 当前用户是否已拥有该角色 */
  hasRole?: boolean
}

/**
 * 分页查询角色
 */
export function getRolePage(pageNum: number, pageSize: number, data: SysRoleDto) {
  return request<PageResult<SysRole>>({
    url: '/system/role/list',
    method: 'post',
    params: { pageNum, pageSize },
    data,
  })
}

/** 新增角色 */
export function addRole(data: SysRole) {
  return request({ url: '/system/role/add', method: 'post', data })
}

/** 编辑角色 */
export function editRole(data: SysRole) {
  return request({ url: '/system/role/edit', method: 'post', data })
}

/** 删除角色 */
export function delRole(id: number) {
  return request({ url: '/system/role/del', method: 'get', params: { id } })
}

/** 批量删除角色 */
export function batchDelRole(ids: number[]) {
  return request({ url: '/system/role/batchDel', method: 'post', data: ids })
}

/**
 * 获取某用户已有的角色列表（含 hasRole 标记）
 */
export function getUserRole(userId: number) {
  return request<UserRoleVo[]>({
    url: '/system/role/getUserRole',
    method: 'get',
    params: { userId },
  })
}

/**
 * 编辑用户角色（授权）
 */
export function editUserRole(data: { userId: number; roleIdList: number[] }) {
  return request({ url: '/system/role/editUserRole', method: 'post', data })
}
