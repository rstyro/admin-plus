package com.lrs.core.admin.service;

import com.lrs.core.admin.entity.Role;
import com.lrs.core.admin.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
public interface IUserRoleService extends IService<UserRole> {
    public List<Role> getUserRoles(Long userId) throws Exception;
}
