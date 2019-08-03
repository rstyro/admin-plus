package com.lrs.core.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("admin_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)    //这个注解是因为Long 类型太大返回到前端会出现精度丢失
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    private String password;

    /**
     * 头像地址
     */
    private String picPath;

    private String status;

    @TableField("sessionId")
    private String sessionId;

    private LocalDateTime createTime;

    //表示该属性不为数据库表字段，但又是必须使用的
    @TableField(exist = false)
    private Role role;

}
