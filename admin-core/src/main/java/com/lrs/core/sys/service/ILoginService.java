package com.lrs.core.sys.service;

import com.lrs.core.entity.ResponseModel;
import com.lrs.core.sys.dto.LoginDTO;
import com.lrs.core.sys.entity.Login;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
public interface ILoginService extends IService<Login> {
    public ResponseModel login(LoginDTO dto, HttpSession session) throws Exception;
    public String logout(HttpSession session) throws Exception;
}
