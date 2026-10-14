/**
 * @file 日志接口
 * @description 与后端 SysLoginInfoController、SysOperLogController 对接
 */
import { request } from '@/utils/request'
import type { PageResult } from './user'

/** 登录日志实体（与后端 SysLoginInfo 对应） */
export interface SysLoginInfo {
  id?: number
  /** 登录账号 */
  loginName?: string
  /** 登录IP地址 */
  ipaddr?: string
  /** 登录地点 */
  location?: string
  browser?: string
  os?: string
  /** 登录状态：'0' 成功，'1' 失败 */
  status?: string
  msg?: string
  /** 访问时间 */
  createTime?: string
}

/** 操作日志实体（与后端 SysOperLog 对应） */
export interface SysOperLog {
  id?: number
  /** 模块标题 */
  title?: string
  /** 业务类型：0 其它 1 新增 2 修改 3 删除 */
  businessType?: number
  /** 方法名称 */
  method?: string
  requestMethod?: string
  /** 操作人 */
  operName?: string
  /** 部门名称 */
  deptName?: string
  /** 请求地址 */
  operUrl?: string
  operIp?: string
  operLocation?: string
  /** 请求参数 */
  operParam?: string
  /** 返回参数 */
  jsonResult?: string
  /** 操作状态：0 正常 1 异常 */
  status?: number
  errorMsg?: string
  operTime?: string
  /** 消耗时间（毫秒） */
  costTime?: number
}

/**
 * 分页查询登录日志
 */
export function getLoginInfoPage(pageNum: number, pageSize: number, data: { keyword?: string }) {
  return request<PageResult<SysLoginInfo>>({
    url: '/system/sysLoginInfo/list',
    method: 'post',
    params: { pageNum, pageSize },
    data,
  })
}

/** 删除登录日志 */
export function delLoginInfo(id: number) {
  return request({ url: '/system/sysLoginInfo/del', method: 'get', params: { id } })
}

/** 批量删除登录日志 */
export function batchDelLoginInfo(ids: number[]) {
  return request({ url: '/system/sysLoginInfo/batchDel', method: 'post', data: ids })
}

/**
 * 分页查询操作日志
 */
export function getOperLogPage(pageNum: number, pageSize: number, data: { keyword?: string }) {
  return request<PageResult<SysOperLog>>({
    url: '/system/sysOperLog/list',
    method: 'post',
    params: { pageNum, pageSize },
    data,
  })
}

/** 删除操作日志 */
export function delOperLog(id: number) {
  return request({ url: '/system/sysOperLog/del', method: 'get', params: { id } })
}

/** 批量删除操作日志 */
export function batchDelOperLog(ids: number[]) {
  return request({ url: '/system/sysOperLog/batchDel', method: 'post', data: ids })
}
