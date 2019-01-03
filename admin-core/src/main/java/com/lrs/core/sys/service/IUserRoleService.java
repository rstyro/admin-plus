package com.lrs.core.sys.service;

import com.lrs.core.sys.entity.Role;
import com.lrs.core.sys.entity.UserRole;
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
    public List<Role> getUserRoles(Integer userId) throws Exception;
}
