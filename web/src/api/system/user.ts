/**
 * @file 用户管理接口
 * @description 与后端 SysUserController 对接（保持旧版接口路径不变）
 */
import { request } from '@/utils/request'

/** 用户实体（与后端 SysUser 对应） */
export interface SysUser {
  id?: number
  username: string
  nickName: string
  password?: string
  avatar?: string
  status?: string
  loginIp?: string
  loginDate?: string
  createTime?: string
  updateTime?: string
}

/** 列表入参（与后端 BaseDto 对应） */
export interface BaseDto {
  keyword?: string
}

/** 分页结果（与 MyBatis-Plus Page 对应） */
export interface PageResult<T> {
  records: T[]
  total: number
  current: number
  pages: number
  size: number
}

/**
 * 分页查询用户列表
 * @description pageNum/pageSize 走 query 参数（旧版约定），keyword 走 body
 */
export function getUserPage(pageNum: number, pageSize: number, data: BaseDto) {
  return request<PageResult<SysUser>>({
    url: '/system/user/list',
    method: 'post',
    params: { pageNum, pageSize },
    data,
  })
}

/** 新增用户 */
export function addUser(data: SysUser) {
  return request({ url: '/system/user/add', method: 'post', data })
}

/** 编辑用户 */
export function editUser(data: SysUser) {
  return request({ url: '/system/user/edit', method: 'post', data })
}

/** 删除用户（旧版用 GET + id query） */
export function delUser(id: number) {
  return request({ url: '/system/user/del', method: 'get', params: { id } })
}

/** 批量删除 */
export function batchDelUser(ids: number[]) {
  return request({ url: '/system/user/batchDel', method: 'post', data: ids })
}
