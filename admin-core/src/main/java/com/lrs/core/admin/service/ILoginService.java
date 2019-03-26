package com.lrs.core.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.common.constant.ResponseModel;
import com.lrs.common.constant.Result;
import com.lrs.core.admin.dto.LoginDTO;
import com.lrs.core.admin.entity.Login;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
public interface ILoginService extends IService<Login> {
    public Result login(LoginDTO dto, HttpSession session) throws Exception;
    public String logout(HttpSession session) throws Exception;
}
