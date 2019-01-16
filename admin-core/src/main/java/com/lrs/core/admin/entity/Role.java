package com.lrs.core.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 最大权限的值
     */
    private BigInteger rights;

    /**
     * 添加权限
     */
    private BigInteger addQx;

    /**
     * 删除权限
     */
    private BigInteger delQx;

    /**
     * 编辑权限
     */
    private BigInteger editQx;

    /**
     * 查看权限
     */
    private BigInteger queryQx;

    private String userId;

    private LocalDateTime createTime;

    //传参
    @TableField(exist = false)
    private String ids;
    //传参
    @TableField(exist = false)
    private String qx;

    @TableField(exist = false)
    private boolean checked;

    public Role() {
    }

    public Role(BigInteger rights, BigInteger addQx, BigInteger delQx, BigInteger editQx, BigInteger queryQx) {
        this.rights = rights;
        this.addQx = addQx;
        this.delQx = delQx;
        this.editQx = editQx;
        this.queryQx = queryQx;
    }

    public Role(Integer roleId, String roleName, String roleDesc, BigInteger rights, BigInteger addQx, BigInteger delQx, BigInteger editQx, BigInteger queryQx, String userId, LocalDateTime createTime) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.rights = rights;
        this.addQx = addQx;
        this.delQx = delQx;
        this.editQx = editQx;
        this.queryQx = queryQx;
        this.userId = userId;
        this.createTime = createTime;
    }

    public Role(Integer roleId, String roleName, String roleDesc, BigInteger rights, BigInteger addQx, BigInteger delQx, BigInteger editQx, BigInteger queryQx, String userId, LocalDateTime createTime, String ids) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.rights = rights;
        this.addQx = addQx;
        this.delQx = delQx;
        this.editQx = editQx;
        this.queryQx = queryQx;
        this.userId = userId;
        this.createTime = createTime;
        this.ids = ids;
    }
}
