package com.lrs.core.system.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户权限VO
 */
@Accessors(chain = true)
@Data
public class UserRoleVo {
    private List<String> permissionList;
    private List<String> roleNameList;
}
