package com.lrs.core.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.system.dto.MenuDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.vo.RoleMenuTreeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenu> getMenuListByParentId(Long parentId);
    List<SysMenu> getAllMenuList();
    List<RoleMenuTreeVo> getAllRoleMenuList(Long roleId);
    Page<SysMenu> getMenuPage(Page page, MenuDto menuDto);
    boolean add(SysMenu sysMenu);
    boolean edit(SysMenu sysMenu);
    boolean del(Long id);
    boolean batchDel(List<Long> ids);
}
