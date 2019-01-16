package com.lrs.core.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.common.constant.ResponseModel;
import com.lrs.core.admin.dto.UserDTO;
import com.lrs.core.admin.entity.User;

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
