package com.lrs.core.system.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.Const;
import com.lrs.common.constant.SystemConst;
import com.lrs.common.exception.ApiException;
import com.lrs.common.utils.RedisSimulation;
import com.lrs.common.vo.TabsVo;
import com.lrs.core.base.BaseController;
import com.lrs.core.config.CommonConfig;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.dto.LoginDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.entity.SysRoleMenu;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.entity.SysUserRole;
import com.lrs.core.system.event.LoginInfoEvent;
import com.lrs.core.system.mapper.SysUserMapper;
import com.lrs.core.system.service.ISysMenuService;
import com.lrs.core.system.service.ISysRoleMenuService;
import com.lrs.core.system.service.ISysUserRoleService;
import com.lrs.core.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Resource
    private ISysRoleMenuService sysMenuRoleService;

    @Resource
    private ISysMenuService sysMenuService;

    @Resource
    private CommonConfig.UserConfig userConfig;

    @Resource
    private RedisSimulation redisSimulation;


    /**
     * 获取用户的菜单权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserPermissionList(Long userId) {
        return buildPermit(getUserMenuList(userId));
    }

    public List<String> buildPermit(List<SysMenu> sysMenus) {
        Set<String> allPermission = new HashSet<>();
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.isHasPermit()) {
                allPermission.add(sysMenu.getPermit());
                if (!CollectionUtils.isEmpty(sysMenu.getChild()) && sysMenu.isHasPermit()) {
                    allPermission.addAll(buildPermit(sysMenu.getChild()));
                }
            }
        }
        return new ArrayList<>(allPermission);
    }


    /**
     * 获取用户的角色ID列表
     *
     * @param userId 用户id
     * @return 角色ID列表
     */
    @Override
    public List<String> getUserRoleList(Long userId) {
        List<SysUserRole> userRoleList = Optional.ofNullable(sysUserRoleService
                .list(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId))).orElse(new ArrayList<>());
        return userRoleList.stream().map(i -> String.valueOf(i.getRoleId())).collect(Collectors.toList());
    }

    /**
     * 获取用户树形菜单
     *
     * @param userId 用户ID
     */
    @Override
    public List<SysMenu> getUserMenuList(Long userId) {
        //获取所有树形菜单
        List<SysMenu> allMenuTreeList = sysMenuService.getAllMenuList();
        List<String> userRoleList = getUserRoleList(userId);
        // 得到用户有权限的菜单
        List<Long> menuList = Optional.ofNullable(userRoleList)
                .filter(i -> !i.isEmpty())
                .map(list -> sysMenuRoleService.list(new LambdaQueryWrapper<SysRoleMenu>()
                        .in(SysRoleMenu::getRoleId, list))
                        .stream()
                        .map(SysRoleMenu::getMenuId)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        // 遍历全菜单，过滤用户有权限的菜单，设置hasPermit字段
        checkPermit(allMenuTreeList, menuList, userId);
        return allMenuTreeList;
    }

    public void checkPermit(List<SysMenu> allMenuTreeList, List<Long> menuList, Long userId) {
        for (SysMenu sysMenu : allMenuTreeList) {
            //判断菜单是否有查看权限,1=admin 超级管理员相当可无视规则得到所有权限
            sysMenu.setHasPermit(menuList.contains(sysMenu.getId()) || userId.equals(1l));
            if (!ObjectUtils.isEmpty(sysMenu.getChild())) {
                checkPermit(sysMenu.getChild(), menuList, userId);
            }
        }
    }

    /**
     * 只返回用户拥有权限的菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<TabsVo> getTabMenuList(Long userId) {
        List<SysMenu> userMenuList = getUserMenuList(StpUtil.getLoginId(0l));
        return userMenuList.stream()
                .filter(SysMenu::isHasPermit)
                .map(this::convertToTabsVo)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SysUser> getUserPage(Page page, BaseDto dto) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        // 排除密码字段和盐字段
        queryWrapper.select(SysUser.class, i -> !i.getColumn().equals("password") && !i.getColumn().equals("salt"));
        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            queryWrapper.like(SysUser::getNickName, dto.getKeyword());
            queryWrapper.or().like(SysUser::getUsername, dto.getKeyword());
        }
        queryWrapper.orderByDesc(SysUser::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    public boolean add(SysUser item) {
        checkUserName(item);
        String salt = IdUtil.fastSimpleUUID();
        item.setSalt(salt);
        item.setPassword(SecureUtil.md5(item.getPassword() + salt));
        return save(item);
    }

    public void checkUserName(SysUser item){
        long count = count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, item.getUsername()));
        if (count > 0) {
            throw new ApiException(ApiResultEnum.SYSTEM_USER_EXIST);
        }
    }
    @Override
    public boolean edit(SysUser item) {
        SysUser sysUser = getById(item.getId());
        if (sysUser == null) {
            throw new ApiException(ApiResultEnum.SYSTEM_USER_NOT_FOUND);
        }
        // 重命名判断
        if (!sysUser.getUsername().equals(item.getUsername())) {
            SysUser existingUser = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, item.getUsername()));
            if (existingUser != null && !existingUser.getId().equals(item.getId())) {
                throw new ApiException(ApiResultEnum.SYSTEM_USER_EXIST);
            }
        }
        // 密码盐从初始化后，不可更改
        item.setSalt(null);
        if (!ObjectUtils.isEmpty(item.getPassword())) {
            item.setPassword(SecureUtil.md5(item.getPassword() + sysUser.getSalt()));
        }
        boolean update = updateById(item);
        if (update && sysUser.getId().equals(StpUtil.getLoginIdAsLong())) {
            // 刷新用户session
            StpUtil.getSession().set(Const.SessionKey.SESSION_USER, getById(item.getId()));
        }
        return update;
    }

    @Override
    public boolean del(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDel(List<Long> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public SysUser login(HttpServletRequest request, LoginDto dto) {
        String errKey = Const.RedisKey.USER_ACCOUNT_ERR_KEY+dto.getUsername()+ BaseController.getRemoteIP(request);
        int errorNumber = ObjectUtil.defaultIfNull(redisSimulation.get(errKey), 0);
        // 锁定时间内登录 则踢出
        int maxRetryCount = userConfig.getMaxRetryCount();
        if (errorNumber >= maxRetryCount) {
            recordLoginInfo(request,dto.getUsername(), SystemConst.LoginInfoStatus.FAIL,ApiResultEnum.SYSTEM_USER_ABOVE_MAX_RETRY_COUNT.getMessage());
            throw new ApiException(ApiResultEnum.SYSTEM_USER_ABOVE_MAX_RETRY_COUNT);
        }
        String codeStr = (String) request.getSession().getAttribute(Const.SessionKey.SESSION_CODE);
        checkLoginError(request, dto.getUsername(),errKey,errorNumber,maxRetryCount,ApiResultEnum.SYSTEM_CODE_ERROR,()->!dto.getCode().equalsIgnoreCase(codeStr));
        SysUser sysUser = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
        checkLoginError(request, dto.getUsername(),errKey,errorNumber,maxRetryCount,ApiResultEnum.SYSTEM_ACCOUNT_NOT_FOUND,()->sysUser==null);
        String salt = sysUser.getSalt();
        String pwd = SecureUtil.md5(dto.getPassword() + salt);
        checkLoginError(request, dto.getUsername(),errKey,errorNumber,maxRetryCount,ApiResultEnum.SYSTEM_PASSWORD_ERROR,()->!pwd.equals(sysUser.getPassword()));
        // 登录成功
        StpUtil.login(sysUser.getId(), dto.isRememberMe());
        recordLoginInfo(request,dto.getUsername(), SystemConst.LoginInfoStatus.SUCCESS,"登录成功");
        // 更新最后登录时间
        updateUserRecord(sysUser);
        SaSession session = StpUtil.getSession();
        session.set(Const.SessionKey.SESSION_USER, sysUser);
        // 数据脱敏
        sysUser.setPassword("****");
        sysUser.setSalt("****");
        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
        System.out.println(JSON.toJSONString(keys));
        return sysUser;
    }

    private void checkLoginError(HttpServletRequest request, String username, String errKey, int errorNumber, int maxRetryCount, ApiResultEnum errorEnum, Supplier<Boolean> supplier) {
        if(supplier.get()){
            redisSimulation.set(errKey, ++errorNumber, userConfig.getLockTime(), TimeUnit.MINUTES);
            recordLoginInfo(request, username, SystemConst.LoginInfoStatus.FAIL, errorEnum.getMessage());
            if (errorNumber >= maxRetryCount) {
                recordLoginInfo(request, username, SystemConst.LoginInfoStatus.FAIL, ApiResultEnum.SYSTEM_USER_ABOVE_MAX_RETRY_COUNT.getMessage());
                throw new ApiException(ApiResultEnum.SYSTEM_USER_ABOVE_MAX_RETRY_COUNT);
            }
            throw new ApiException(errorEnum);
        }
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        SysUser sysUser = Convert.convert(SysUser.class, StpUtil.getSession().get(Const.SessionKey.SESSION_USER));
        recordLoginInfo(request,sysUser.getUsername(), SystemConst.LoginInfoStatus.SUCCESS,"退出登录");
        StpUtil.logout();
        return true;
    }

    /**
     * 记录登录信息
     * @param request request
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLoginInfo(HttpServletRequest request,String username, String status, String message) {
        LoginInfoEvent loginInfoEvent = new LoginInfoEvent();
        loginInfoEvent.setLoginName(username);
        loginInfoEvent.setStatus(status);
        loginInfoEvent.setMsg(message);
        loginInfoEvent.setCreateTime(LocalDateTime.now());
        loginInfoEvent.setInfo(request);
        SpringUtil.getApplicationContext().publishEvent(loginInfoEvent);
    }

    public void updateUserRecord(SysUser sysUser){
        // 更新最后登录时间
        sysUser.setLoginDate(LocalDateTime.now());
        sysUser.setLoginIp(NetUtil.getLocalhostStr());
        updateById(sysUser);
    }

    /**
     * 转换为前端页面tab菜单
     *
     * @param menu
     * @return
     */
    private TabsVo convertToTabsVo(SysMenu menu) {
        TabsVo tabsVo = new TabsVo();
        tabsVo.setIcon(menu.getIcon());
        tabsVo.setId(String.valueOf(menu.getId()));
        tabsVo.setText(menu.getMenuName());
        tabsVo.setUrl(menu.getMenuUrl());
        List<TabsVo> child = Optional.ofNullable(menu.getChild())
                .orElse(Collections.emptyList()).stream()
                .filter(SysMenu::isHasPermit)
                .filter(i -> i.getMenuType().equals(1) || i.getMenuType().equals(0))
                .map(this::convertToTabsVo)
                .collect(Collectors.toList());
        tabsVo.setChildren(child);
        return tabsVo;
    }
}
