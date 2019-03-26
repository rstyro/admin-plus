package com.lrs.core.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.Result;
import com.lrs.common.dto.PageDTO;
import com.lrs.common.exception.TryAgainException;
import com.lrs.core.act.entity.Acticle;
import com.lrs.core.act.mapper.ActicleMapper;
import com.lrs.core.act.service.IActicleService;
import com.lrs.core.aspect.IsTryAgain;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2019-03-26
 */
@Service
public class ActicleServiceImpl extends ServiceImpl<ActicleMapper, Acticle> implements IActicleService{

    @Override
    public Result getList(PageDTO dto) throws Exception {
        IPage<Acticle> page = new Page<>();
        if(dto.getPageNo() != null){
            page.setCurrent(dto.getPageNo());
        }
        if(dto.getPageSize() != null){
            page.setSize(dto.getPageSize());
        }
        QueryWrapper<Acticle> queryWrapper = new QueryWrapper();
    //        if(!StringUtils.isEmpty(dto.getKeyword())){
    //            queryWrapper.lambda()
    //                    .like(Acticle::getAuther,dto.getKeyword())
    //                    .like(Acticle::getContent,dto.getKeyword())
    //                    .like(Acticle::getTitle,dto.getKeyword());
    //        }
        IPage<Acticle> iPage = this.page(page, queryWrapper);
        return Result.ok(iPage);
    }

    @Override
    public Result add(Acticle item, HttpSession session) throws Exception {
        this.save(item);
        return Result.ok();
    }

    /**
     * 更新失败就重试
     * @param item
     * @param session
     * @return
     * @throws Exception
     */
    @IsTryAgain
    public Result edit(Acticle item, HttpSession session) throws Exception {
        if(!this.updateById(item)){
            throw new TryAgainException(ApiResultEnum.ERROR);
        }
       return Result.ok();
    }

    @Override
    public Result del(Long id, HttpSession session) throws Exception {
        this.removeById(id);
        return Result.ok();
    }

    @Override
    public Result getDetail(Long id) throws Exception {
    Acticle item = this.getOne(new QueryWrapper<Acticle>().lambda().eq(Acticle::getId,id));
         return Result.ok(item);
    }
}
