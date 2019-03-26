package com.lrs.core.act.service;

import com.lrs.core.act.entity.Acticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.common.constant.Result;
import com.lrs.common.dto.PageDTO;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *   服务类
 * </p>
 *
 * @author rstyro
 * @since 2019-3-26
 */
public interface IActicleService extends IService<Acticle> {
    public Result getList(PageDTO dto) throws  Exception;
    public Result add(Acticle item, HttpSession session) throws  Exception;
    public Result edit(Acticle item, HttpSession session) throws  Exception;
    public Result del(Long id, HttpSession session) throws  Exception;
    public Result getDetail(Long id) throws  Exception;
}
