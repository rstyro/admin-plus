package com.lrs.core.sys.mapper;

import com.lrs.core.sys.entity.Role;
import com.lrs.core.sys.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Component
public interface UserRoleMapper extends BaseMapper<UserRole> {
      List<Role> getUserRoles(Integer userId);
}
