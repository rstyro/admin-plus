package com.lrs.core.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.ResponseModel;
import com.lrs.common.dto.PageDTO;
import com.lrs.core.test.entity.People;
import com.lrs.core.test.mapper.PeopleMapper;
import com.lrs.core.test.service.IPeopleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2019-1-18
 */
@Service
public class PeopleServiceImpl extends ServiceImpl<PeopleMapper, People> implements IPeopleService {

    @Override
    public ResponseModel getList(PageDTO dto) throws Exception {
        IPage<People> page = new Page<>();
        if(dto.getPageNo() != null){
            page.setCurrent(dto.getPageNo());
        }
        if(dto.getPageSize() != null){
            page.setSize(dto.getPageSize());
        }
        QueryWrapper<People> queryWrapper = new QueryWrapper();
    //        if(!StringUtils.isEmpty(dto.getKeyword())){
    //            queryWrapper.lambda()
    //                    .like(People::getAuther,dto.getKeyword())
    //                    .like(People::getContent,dto.getKeyword())
    //                    .like(People::getTitle,dto.getKeyword());
    //        }
        IPage<People> iPage = this.page(page, queryWrapper);
        return new ResponseModel(ApiResultEnum.SUCCESS,iPage);
    }

    @Override
    public ResponseModel add(People item, HttpSession session) throws Exception {
        this.save(item);
        return new ResponseModel(ApiResultEnum.SUCCESS,null);
    }

    @Override
    public ResponseModel edit(People item, HttpSession session) throws Exception {
        this.updateById(item);
        return new ResponseModel(ApiResultEnum.SUCCESS,null);
    }

    @Override
    public ResponseModel del(Long id, HttpSession session) throws Exception {
        this.removeById(id);
        return new ResponseModel(ApiResultEnum.SUCCESS,null);
    }

    @Override
    public ResponseModel getDetail(Long id) throws Exception {
    People item = this.getOne(new QueryWrapper<People>().lambda().eq(People::getId,id));
        return new ResponseModel(ApiResultEnum.SUCCESS,item);
    }
}
