package com.lrs.core.system.vo;

import lombok.Data;

import java.util.List;

/**
 * 角色选择菜单Vo
 */
@Data
public class RoleMenuTreeVo {

    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;


    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 该角色是否拥有菜单权限
     */
    private Boolean hasRoleFlag;

    /**
     * 子类
     */
    private List<RoleMenuTreeVo> child;


}
