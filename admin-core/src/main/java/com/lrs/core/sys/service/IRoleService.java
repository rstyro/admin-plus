package com.lrs.core.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.entity.ResponseModel;
import com.lrs.core.sys.dto.QxDTO;
import com.lrs.core.sys.entity.Role;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
public interface IRoleService extends IService<Role> {

    public List<Role> getRolelist() throws  Exception;
    public ResponseModel getMenu(QxDTO dto) throws  Exception;
    public ResponseModel edit(Role role) throws  Exception;
    public ResponseModel add(Role role, HttpSession session) throws  Exception;
    public ResponseModel del(Integer roleId) throws  Exception;

}
