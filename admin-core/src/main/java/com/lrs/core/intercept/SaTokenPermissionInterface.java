package com.lrs.core.intercept;

import cn.dev33.satoken.stp.StpInterface;
import com.lrs.core.system.service.ISysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * saToken权限
 */
@Component
public class SaTokenPermissionInterface implements StpInterface {
    @Resource
    private ISysUserService sysUserService;


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong((String) loginId);
        List<String> userPermissionList = sysUserService.getUserPermissionList(userId);
        // 如果userId=1代表admin用户，给他添加admin权限
        if(userId==1){
            userPermissionList.add("admin");
        }
        return userPermissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return sysUserService.getUserRoleList((Long) loginId);
    }
}
