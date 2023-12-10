package com.lrs.core.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysBtn;
import com.lrs.core.system.mapper.SysBtnMapper;
import com.lrs.core.system.service.ISysBtnService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
@Service
public class SysBtnServiceImpl extends ServiceImpl<SysBtnMapper, SysBtn> implements ISysBtnService {

    @Override
    public Page<SysBtn> getPage(Page page, BaseDto dto) {
        LambdaQueryWrapper<SysBtn> queryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            queryWrapper.like(SysBtn::getRemark, dto.getKeyword());
        }
        queryWrapper.orderByAsc(SysBtn::getId);
        return page(page, queryWrapper);
    }

    @Override
    public boolean add(SysBtn item) {
        return save(item);
    }

    @Override
    public boolean edit(SysBtn item) {
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
