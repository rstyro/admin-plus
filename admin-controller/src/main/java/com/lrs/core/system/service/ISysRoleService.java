package com.lrs.core.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.system.dto.SysRoleDto;
import com.lrs.core.system.entity.SysRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
public interface ISysRoleService extends IService<SysRole> {

    Page<SysRole> getRolePage(Page page, SysRoleDto menuDto);
    boolean add(SysRole item);
    boolean edit(SysRole item);
    boolean del(Long id);
    boolean batchDel(List<Long> ids);
    List<SysRole> getUserRoleList(Long userId);
    boolean editUserRole(SysRoleDto dto);
}
