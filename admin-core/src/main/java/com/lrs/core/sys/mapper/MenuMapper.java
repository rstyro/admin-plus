package com.lrs.core.sys.mapper;

import com.lrs.core.sys.entity.Menu;
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
