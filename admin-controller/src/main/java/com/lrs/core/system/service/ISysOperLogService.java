package com.lrs.core.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysOperLog;
import com.lrs.core.system.event.OperLogEvent;

import java.util.List;


/**
 * <p>
 *  操作日志记录 服务类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-11
 */
public interface ISysOperLogService extends IService<SysOperLog> {

    //日志时间
    void recordOper(OperLogEvent operLogEvent);

    Page<SysOperLog> getPage(Page page, BaseDto dto);
}
