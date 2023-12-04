package com.lrs.core.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysBtn;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
public interface ISysBtnService extends IService<SysBtn> {

    Page<SysBtn> getPage(Page page, BaseDto dto);
    boolean add(SysBtn item);
    boolean edit(SysBtn item);
    boolean del(Long id);
    boolean batchDel(List<Long> ids);

}
