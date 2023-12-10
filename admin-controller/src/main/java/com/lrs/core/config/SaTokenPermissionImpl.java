package com.lrs.core.config;

import cn.dev33.satoken.stp.StpInterface;
import com.lrs.core.system.service.ISysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * saToken权限，自己实现一下用户的权限
 */
@Component
public class SaTokenPermissionImpl implements StpInterface {
    @Resource
    private ISysUserService sysUserService;


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong((String) loginId);
        List<String> userPermissionList = sysUserService.getUserPermissionList(userId);
        // 如果userId=1代表admin用户，给他添加admin权限
        return userPermissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return sysUserService.getUserRoleList((Long) loginId);
    }
}
