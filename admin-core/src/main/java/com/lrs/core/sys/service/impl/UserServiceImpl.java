package com.lrs.core.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lrs.common.utils.Tools;
import com.lrs.common.utils.encrypt.SHA;
import com.lrs.core.entity.ApiResultEnum;
import com.lrs.core.entity.ResponseModel;
import com.lrs.core.exception.ApiException;
import com.lrs.core.sys.dto.UserDTO;
import com.lrs.core.sys.entity.Role;
import com.lrs.core.sys.entity.User;
import com.lrs.core.sys.entity.UserRole;
import com.lrs.core.sys.mapper.RoleMapper;
import com.lrs.core.sys.mapper.UserMapper;
import com.lrs.core.sys.mapper.UserRoleMapper;
import com.lrs.core.sys.service.IRoleService;
import com.lrs.core.sys.service.IUserRoleService;
import com.lrs.core.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @Override
    public List<User> getUserList() throws Exception{
        return this.list(new QueryWrapper<User>());
    }

    @Override
    public ResponseModel getRole(String userId) throws Exception{
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId));
        List<Role> roles = roleService.list(new QueryWrapper<Role>());
        for (Role role:roles){
            for(UserRole userRole:userRoles){
                if(userRole.getRoleId().intValue() == role.getRoleId().intValue()){
                    role.setChecked(true);
                    break;
                }else {
                    role.setChecked(false);
                }
            }
        }
        return new ResponseModel(roles);
    }

    @Override
    public ResponseModel add(User user) throws Exception{
        if(StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())){
            throw new ApiException(ApiResultEnum.PARAMETER_NULL,null);
        }
        User userVo = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, user.getUsername()));
        if(userVo != null){
            throw  new ApiException(ApiResultEnum.ACCOUNT_EXIST,null);
        }
        user.setPassword(SHA.encryptSHA(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        this.save(user);
        return new ResponseModel(null);
    }

    @Override
    public ResponseModel edit(User user) throws Exception{
        if(!StringUtils.isEmpty(user.getPassword())){
            user.setPassword(SHA.encryptSHA(user.getPassword()));
        }
        this.saveOrUpdate(user);
        return new ResponseModel(null);
    }

    @Override
    public ResponseModel editRole(UserDTO dto) throws Exception{
        userRoleService.remove(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId,dto.getUserId()));
        String strIds = dto.getIds();
        //移除之前的
        userRoleService.remove(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId,dto.getUserId()));
        if(!StringUtils.isEmpty(strIds)){
            String[] ids = strIds.split(",");
            List<UserRole> userRoles = new ArrayList<>();
            for(String roleId:ids){
                UserRole userRole = new UserRole();
                userRole.setUserId(dto.getUserId());
                userRole.setRoleId(Integer.parseInt(roleId));
                userRoles.add(userRole);
            }
            if(userRoles.size() > 0){
                userRoleService.saveBatch(userRoles,1000);
            }
        }
        return new ResponseModel(null);
    }

    @Override
    public ResponseModel del(String userId) throws Exception{
        this.removeById(Integer.parseInt(userId));
        return new ResponseModel(null);
    }

    @Override
    public User getUserInfo(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(user.getUsername())){
            queryWrapper.lambda().eq(User::getUsername,user.getUsername());
        }
        if(!StringUtils.isEmpty(user.getPassword())){
            queryWrapper.lambda().eq(User::getPassword, SHA.encryptSHA(user.getPassword()));
        }
        if(!StringUtils.isEmpty(user.getUserId())){
            queryWrapper.lambda().eq(User::getUserId,user.getUserId());
        }
        User uerVo = this.getOne(queryWrapper);
        return uerVo;
    }
}
