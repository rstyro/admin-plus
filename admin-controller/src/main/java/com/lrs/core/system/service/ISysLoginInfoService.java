package com.lrs.core.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysLoginInfo;
import com.lrs.core.system.event.LoginInfoEvent;

import java.util.List;


/**
 * <p>
 *  系统访问记录 服务类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-12
 */
public interface ISysLoginInfoService extends IService<SysLoginInfo> {

    //保存日志
    void saveLoginInfoRecord(LoginInfoEvent loginInfoEvent);

    Page<SysLoginInfo> getPage(Page page, BaseDto dto);
    boolean add(SysLoginInfo item);
    boolean edit(SysLoginInfo item);
    boolean del(Long id);
    boolean batchDel(List<Long> ids);
}
