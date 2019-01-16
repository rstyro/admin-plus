package com.lrs.core.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.Const;
import com.lrs.common.constant.ResponseModel;
import com.lrs.core.act.entity.Acticle;
import com.lrs.core.act.entity.ActicleDTO;
import com.lrs.core.act.mapper.ActicleMapper;
import com.lrs.core.act.service.IActicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2019-01-16
 */
@Service
public class ActicleServiceImpl extends ServiceImpl<ActicleMapper, Acticle> implements IActicleService {

    @Override
    public ResponseModel getList(ActicleDTO dto) throws Exception {
        IPage<Acticle> page = new Page<>();
        if(dto.getPage() != null){
            page.setCurrent(dto.getPage());
        }
        if(dto.getPageSize() != null){
            page.setSize(dto.getPageSize());
        }
        IPage<Acticle> iPage = this.page(page, new QueryWrapper<Acticle>().lambda().eq(Acticle::getIsDeleted, Const.NO));
        return new ResponseModel(ApiResultEnum.SUCCESS,iPage);
    }

    @Override
    public ResponseModel add(Acticle item, HttpSession session) throws Exception {
        this.save(item);
        return new ResponseModel(ApiResultEnum.SUCCESS,null);
    }

    @Override
    public ResponseModel edit(Acticle item, HttpSession session) throws Exception {
        this.updateById(item);
        return new ResponseModel(ApiResultEnum.SUCCESS,null);
    }

    @Override
    public ResponseModel del(Long id, HttpSession session) throws Exception {
//        Acticle acticle = this.getById(id);
//        acticle.setIsDeleted(Const.YES);
//        this.updateById(acticle);
        this.removeById(id);
        return new ResponseModel(ApiResultEnum.SUCCESS,null);
    }

    @Override
    public ResponseModel getDetail(Long id) throws Exception {
        return new ResponseModel(ApiResultEnum.SUCCESS,null);
    }
}
