package com.lrs.core.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.Const;
import com.lrs.common.constant.Result;
import com.lrs.common.exception.ApiException;
import com.lrs.common.utils.Tools;
import com.lrs.common.utils.encrypt.SHA;
import com.lrs.common.utils.permission.RightsHelper;
import com.lrs.core.admin.dto.LoginDTO;
import com.lrs.core.admin.entity.Login;
import com.lrs.core.admin.entity.Menu;
import com.lrs.core.admin.entity.Role;
import com.lrs.core.admin.entity.User;
import com.lrs.core.admin.mapper.LoginMapper;
import com.lrs.core.admin.service.ILoginService;
import com.lrs.core.admin.service.IMenuService;
import com.lrs.core.admin.service.IUserRoleService;
import com.lrs.core.admin.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IMenuService menuService;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result login(LoginDTO dto, HttpSession session) throws Exception {
        String psw =dto.getPassword();
        String userName = dto.getUsername();
        if(Tools.isEmpty(userName) || Tools.isEmpty(psw)){
            throw new ApiException(ApiResultEnum.PARAMETER_NULL);
        }
        psw = SHA.encryptSHA(psw);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(User::getUsername,dto.getUsername()).eq(User::getPassword,psw);
        User user = userService.getOne(queryWrapper);
        if(user == null){
            throw  new ApiException(ApiResultEnum.ACCOUNT_NOT_FOUND);
        }
        if("lock".equalsIgnoreCase(user.getStatus())){
            throw new ApiException(ApiResultEnum.ACCOUNT_LOCK);
        }
        //获取用户权限
        Long userId = user.getUserId();
        List<Role> roles =userRoleService.getUserRoles(userId);
        long maxMenuId = menuService.getMaxId();
        Role uRole = new Role(new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"),new BigInteger("0"));
        checkUserRole(roles, uRole, maxMenuId);
        System.out.println("==ROLE=="+JSON.toJSONString(uRole));
        user.setRole(uRole);
        //父级菜单
        List<Menu> parentMenuList = menuService.getAllMenuList();
        checkMenuRole(parentMenuList, uRole.getRights(),user.getUsername());
        ServletContext servletContext = session.getServletContext();
        Map<String,User> globalUser = (Map<String, User>) servletContext.getAttribute(Const.GLOBAL_SESSION);
        if(globalUser == null){
            globalUser = new HashMap<String, User>();
        }else{
            if(globalUser.containsKey(userName)){
                globalUser.remove(userName);
            }
        }
        user.setSessionId(session.getId());
        user.setPassword("*****");
        globalUser.put(userName, user);
        session.setMaxInactiveInterval(0);
        session.setAttribute(Const.SESSION_ALL_MENU, parentMenuList);
        session.setAttribute(Const.SESSION_USER, user);
        servletContext.setAttribute(Const.GLOBAL_SESSION, globalUser);
        User updateUser = new User();
        updateUser.setSessionId(session.getId());
        updateUser.setUserId(userId);
        userService.updateById(updateUser);
        //保存登录日志
        Login loginLog = new Login();
        loginLog.setLastLoginTime(LocalDateTime.now());
        loginLog.setUserId(userId);
        this.save(loginLog);
        return Result.ok();
    }

    /**
     * 验证权限
     * admin 用户则不做校验
     *
     * @param oneMenuList
     *            总菜单
     * @param rights
     *            用户权限值
     */
    public void checkMenuRole(List<Menu> oneMenuList, BigInteger rights,String adminUserName) {
        for (Menu m : oneMenuList) {
            m.setHasMenu(RightsHelper.testRights(rights, m.getMenuId())|| adminUserName.equalsIgnoreCase("admin"));
            if (m.isHasMenu()) {
                List<Menu> subList = m.getSubMenu();
                for (Menu subM : subList) {
                    subM.setHasMenu(RightsHelper.testRights(rights, subM.getMenuId()) || adminUserName.equalsIgnoreCase("admin"));
                }
            }
        }
    }

    /**
     * 把用户拥有的角色权限合并起来
     * @param uRoles
     * @param uRole
     * @param maxMenuId
     */
    public void checkUserRole(List<Role> uRoles,Role uRole,long maxMenuId){
        for(int i=0;i<uRoles.size();i++){
            BigInteger right = uRoles.get(i).getRights();
            System.out.println("right="+right);
            BigInteger aqx = uRoles.get(i).getAddQx();
            BigInteger dqx = uRoles.get(i).getDelQx();
            BigInteger eqx = uRoles.get(i).getEditQx();
            BigInteger qqx = uRoles.get(i).getQueryQx();
            for(int j=0;j<=maxMenuId;j++){
                if(right.testBit(j) ){
                    uRole.setRights(uRole.getRights().setBit(j));
                }
                if(aqx.testBit(j)){
                    uRole.setAddQx(uRole.getAddQx().setBit(j));
                }
                if(dqx.testBit(j)){
                    uRole.setDelQx(uRole.getDelQx().setBit(j));
                }
                if(eqx.testBit(j)){
                    uRole.setEditQx(uRole.getEditQx().setBit(j));
                }
                if(qqx.testBit(j)){
                    uRole.setQueryQx(uRole.getQueryQx().setBit(j));
                }
            }
            System.out.println("===i="+i+"==ROLE=="+JSON.toJSONString(uRole));
        }
    }

    @Override
    public String logout(HttpSession session) throws Exception {
        session.removeAttribute(Const.SESSION_ALL_MENU);
        session.removeAttribute(Const.SESSION_USER);
        session.removeAttribute(Const.SESSION_QX);
        return "login";
    }
}
