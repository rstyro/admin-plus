package com.lrs.core.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lrs.common.sensitive.SensitiveService;
import org.springframework.stereotype.Service;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 */
@Service
public class SysSensitiveServiceImpl implements SensitiveService {

    /**
     * 是否脱敏
     */
    @Override
    public boolean isSensitive(String roleKey, String perms) {
        if (!StpUtil.isLogin()) {
            return true;
        }
        if(StpUtil.getLoginIdAsLong()==1){
            return false;
        }
        boolean roleExist = StringUtils.isNotBlank(roleKey);
        boolean permsExist = StringUtils.isNotBlank(perms);
        if (roleExist && permsExist) {
            if (StpUtil.hasRole(roleKey) && StpUtil.hasPermission(perms)) {
                return false;
            }
        } else if (roleExist && StpUtil.hasRole(roleKey)) {
            return false;
        } else if (permsExist && StpUtil.hasPermission(perms)) {
            return false;
        }
        return true;
    }

}
