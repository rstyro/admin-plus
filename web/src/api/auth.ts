/**
 * @file 认证相关接口
 * @description 登录、登出、当前用户认证信息
 *   后端 AuthController.login 返回 R<SysUser>，data 字段是用户对象（不是 token）
 *   token 由 sa-token 通过 Set-Cookie 下发，前端从 cookie 读取
 */
import { request } from '@/utils/request'

/** 登录入参（与后端 LoginDto 对应） */
export interface LoginDto {
  username: string
  password: string
  /** 验证码 */
  code: string
  /** 记住我 */
  rememberMe?: boolean
}

/**
 * 当前登录用户信息
 * @description 与后端 SysUser 实体对应（/auth/info 返回的 user 已清除密码与盐）
 */
export interface UserInfo {
  id?: number
  username: string
  nickName: string
  avatar?: string
  status?: string
  loginIp?: string
  loginDate?: string
  createTime?: string
  updateTime?: string
}

/**
 * 认证信息（后端 GET /auth/info）
 * @description 权限口径与 Sa-Token 服务端校验一致：取自用户有权限的 sys_menu.permit
 */
export interface AuthInfo {
  user: UserInfo
  /** 角色名称列表 */
  roles: string[]
  /** 权限 KEY 列表，v-permission 依据 */
  permissions: string[]
  /** 系统名称（后端 admin.common.systemName） */
  systemName?: string
}

/**
 * 用户登录
 * @param data 登录入参
 * @returns R<SysUser>，data 是用户信息（token 在 cookie 里，不在 body）
 */
export function login(data: LoginDto) {
  return request<UserInfo>({
    url: '/login',
    method: 'post',
    data,
  })
}

/**
 * 用户登出
 * @description 后端 StpUtil.logout() 会让 sa-token cookie 失效
 */
export function logout() {
  return request({
    url: '/logout',
    method: 'post',
  })
}

/**
 * 获取当前登录用户的认证信息（角色、权限 KEY、系统名）
 */
export function getAuthInfo() {
  return request<AuthInfo>({
    url: '/auth/info',
    method: 'get',
  })
}
