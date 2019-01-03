package com.lrs.core.sys.service;

import com.lrs.core.entity.ResponseModel;
import com.lrs.core.sys.dto.LoginDTO;
import com.lrs.core.sys.dto.UserDTO;
import com.lrs.core.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
public interface IUserService extends IService<User> {
    public List<User> getUserList() throws  Exception;
    public ResponseModel getRole(String userId) throws  Exception;
    public ResponseModel add(User user) throws  Exception;
    public ResponseModel edit(User user) throws  Exception;
    public ResponseModel editRole(UserDTO dto) throws  Exception;
    public ResponseModel del(String userId) throws  Exception;
    public User getUserInfo(User user) throws  Exception;
}
