package com.lrs.core.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysLoginInfo;
import com.lrs.core.system.event.LoginInfoEvent;
import com.lrs.core.system.mapper.SysLoginInfoMapper;
import com.lrs.core.system.service.ISysLoginInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-12
 */
@Slf4j
@Service
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoMapper, SysLoginInfo> implements ISysLoginInfoService {


    @Override
    @Async
    @EventListener
    public void saveLoginInfoRecord(LoginInfoEvent loginInfoEvent) {
        try {
            SysLoginInfo loginInfo = new SysLoginInfo();
            BeanUtil.copyProperties(loginInfoEvent, loginInfo);
            save(loginInfo);
        } catch (Exception e) {
            log.error("保存操作日志事件推送失败，err={}", e.getMessage(), e);
        }
    }

    @Override
    public Page<SysLoginInfo> getPage(Page page, BaseDto dto) {
        LambdaQueryWrapper<SysLoginInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            queryWrapper.like(SysLoginInfo::getLoginName, dto.getKeyword())
                    .or().like(SysLoginInfo::getLocation, dto.getKeyword());
        }
        queryWrapper.orderByDesc(SysLoginInfo::getId);
        return page(page, queryWrapper);
    }

    @Override
    public boolean add(SysLoginInfo item) {
        return save(item);
    }

    @Override
    public boolean edit(SysLoginInfo item) {
        return updateById(item);
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
