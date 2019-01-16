package com.lrs.core.act.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("act_acticle")
public class Acticle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String auther;

    private String title;

    private String content;

    /**
     * Y/N
     * 逻辑删除的注解
     */
    @TableLogic
    private String isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


}
