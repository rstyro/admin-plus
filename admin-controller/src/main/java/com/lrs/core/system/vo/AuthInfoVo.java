package com.lrs.core.system.vo;

import com.lrs.core.system.entity.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 当前登录用户的认证信息
 * @description 前后端分离后供 web 前端做按钮级权限控制与系统名展示
 *   （旧版由 Thymeleaf 方言 sa:hasPermissionOr 在服务端完成，无需此对象）
 */
@Data
@Accessors(chain = true)
public class AuthInfoVo {

    /**
     * 当前登录用户（已清除密码与盐）
     */
    private SysUser user;

    /**
     * 角色名称列表
     */
    private List<String> roles;

    /**
     * 权限 KEY 列表，取自用户有权限的菜单/按钮 sys_menu.permit
     */
    private List<String> permissions;

    /**
     * 系统名称
     */
    private String systemName;
}
