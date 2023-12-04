package com.lrs.core.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.common.vo.TabsVo;
import com.lrs.core.system.dto.LoginDto;
import com.lrs.core.system.dto.SysUserDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
public interface ISysUserService extends IService<SysUser> {
    // 获取用户的权限列表
    List<String> getUserPermissionList(Long userId);

    // 获取用户的角色列表
    List<String> getUserRoleList(Long userId);
    // 返回所有菜单，并判断用户是否拥有该菜单的权限
    List<SysMenu> getUserMenuList(Long userId);
    // 只返回用户拥有的菜单
    List<TabsVo> getTabMenuList(Long userId);

    // 分页获取用户列表
    Page<SysUser> getUserPage(Page page, SysUserDto menuDto);
    boolean add(SysUser item);
    boolean edit(SysUser item);
    boolean del(Long id);
    boolean batchDel(List<Long> ids);

    // 登录
    SysUser login(HttpServletRequest request,LoginDto dto);
}
