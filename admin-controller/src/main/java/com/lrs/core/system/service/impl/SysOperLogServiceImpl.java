package com.lrs.core.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.utils.RegionUtils;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysOperLog;
import com.lrs.core.system.event.OperLogEvent;
import com.lrs.core.system.mapper.SysOperLogMapper;
import com.lrs.core.system.service.ISysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-11
 */
@Slf4j
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {


    /**
     * 操作日志记录
     *
     * @param operLogEvent 操作日志事件
     */
    @Async
    @EventListener
    public void recordOper(OperLogEvent operLogEvent) {
        try {
            // 远程查询操作地点
            SysOperLog sysOperLog = new SysOperLog();
            BeanUtil.copyProperties(operLogEvent, sysOperLog);
            sysOperLog.setOperLocation(RegionUtils.getCityInfo(sysOperLog.getOperIp()));
            save(sysOperLog);
        } catch (Exception e) {
            log.error("保存操作日志事件推送失败，err={}", e.getMessage(), e);
        }
    }


    @Override
    public Page<SysOperLog> getPage(Page page, BaseDto dto) {
        LambdaQueryWrapper<SysOperLog> queryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            queryWrapper.like(SysOperLog::getOperName, dto.getKeyword());
        }
        queryWrapper.orderByDesc(SysOperLog::getId);
        return page(page, queryWrapper);
    }

    @Override
    public boolean del(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDel(List<Long> ids) {
        return removeBatchByIds(ids);
    }

}
