package com.lrs.core.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.common.vo.TabsVo;
import com.lrs.core.system.dto.LoginDto;
import com.lrs.core.system.dto.MenuDto;
import com.lrs.core.system.dto.SysUserDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.system.vo.UserRoleVo;
import org.springframework.web.bind.annotation.RequestBody;

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
    List<String> getUserPermissionList(Long userId);
    List<String> getUserRoleList(Long userId);
    List<SysMenu> getUserMenuList(Long userId);
    List<TabsVo> getTabMenuList(Long userId);

    Page<SysUser> getUserPage(Page page, SysUserDto menuDto);
    boolean add(SysUser item);
    boolean edit(SysUser item);
    boolean del(Long id);
    boolean batchDel(List<Long> ids);

    // 登录
    SysUser login(HttpServletRequest request,LoginDto dto);
}
