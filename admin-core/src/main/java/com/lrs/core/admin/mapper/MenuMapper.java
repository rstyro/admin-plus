package com.lrs.core.admin.mapper;

import com.lrs.core.admin.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Component
public interface MenuMapper extends BaseMapper<Menu> {
    public long getMaxMenuId();
}
