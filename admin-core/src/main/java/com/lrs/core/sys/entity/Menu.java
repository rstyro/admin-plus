package com.lrs.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

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
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    /**
     * 父级ID
     */
    private Integer parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单地址
     */
    private String menuUrl;

    /**
     * 1 -- 系统菜单，2 -- 业务菜单
     */
    private String menuType;

    /**
     * 菜单Icon
     */
    private String menuIcon;

    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 创建这个菜单的用户id
     */
    private Integer userId;

    /**
     * 1-- 删除状态，0 -- 正常
     */
    private Integer isDel;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    //子菜单
    @TableField(exist = false)
    private List<Menu> subMenu;

    @TableField(exist = false)
    private boolean hasMenu = false;


}
