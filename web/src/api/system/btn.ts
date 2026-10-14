/**
 * @file 按钮管理接口
 * @description 与后端 SysBtnController 对接
 */
import { request } from '@/utils/request'
import type { PageResult } from './user'

/** 按钮实体 */
export interface SysBtn {
  id?: number
  btnName: string
  btnKey: string
  remark?: string
  createTime?: string
}

/**
 * 分页查询按钮
 */
export function getBtnPage(pageNum: number, pageSize: number, data: { keyword?: string }) {
  return request<PageResult<SysBtn>>({
    url: '/system/btn/list',
    method: 'post',
    params: { pageNum, pageSize },
    data,
  })
}

/** 新增按钮 */
export function addBtn(data: SysBtn) {
  return request({ url: '/system/btn/add', method: 'post', data })
}

/** 编辑按钮 */
export function editBtn(data: SysBtn) {
  return request({ url: '/system/btn/edit', method: 'post', data })
}

/** 删除按钮 */
export function delBtn(id: number) {
  return request({ url: '/system/btn/del', method: 'get', params: { id } })
}

/** 批量删除按钮 */
export function batchDelBtn(ids: number[]) {
  return request({ url: '/system/btn/batchDel', method: 'post', data: ids })
}
