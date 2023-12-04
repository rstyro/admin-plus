package com.lrs.core.system.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.Const;
import com.lrs.common.exception.ApiException;
import com.lrs.common.vo.TabsVo;
import com.lrs.core.system.dto.LoginDto;
import com.lrs.core.system.dto.SysUserDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.entity.SysRoleMenu;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.entity.SysUserRole;
import com.lrs.core.system.mapper.SysUserMapper;
import com.lrs.core.system.service.ISysRoleMenuService;
import com.lrs.core.system.service.ISysMenuService;
import com.lrs.core.system.service.ISysUserRoleService;
import com.lrs.core.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
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

    public List<String> buildPermit(List<SysMenu> sysMenus){
        Set<String> allPermission = new HashSet<>();
        for (SysMenu sysMenu : sysMenus) {
            if(sysMenu.isHasPermit()){
                allPermission.add(sysMenu.getPermit());
                if(!CollectionUtils.isEmpty(sysMenu.getChild()) && sysMenu.isHasPermit()){
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
    public Page<SysUser> getUserPage(Page page, SysUserDto dto) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        // 排除密码字段和盐字段
        queryWrapper.select(SysUser.class,i->!i.getColumn().equals("password") && !i.getColumn().equals("salt"));
        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            queryWrapper.like(SysUser::getNickName, dto.getKeyword());
            queryWrapper.or().like(SysUser::getUsername, dto.getKeyword());
        }
        queryWrapper.orderByDesc(SysUser::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    public boolean add(SysUser item) {
        // 随机盐
        String salt = IdUtil.fastSimpleUUID();
        item.setSalt(salt);
        item.setPassword(SecureUtil.md5(item.getPassword() + salt));
        return save(item);
    }

    @Override
    public boolean edit(SysUser item) {
        item.setPassword(null);
        // 密码盐从初始化后，不可更改
        item.setSalt(null);
        if (!ObjectUtils.isEmpty(item.getPassword())) {
            item.setPassword(SecureUtil.md5(item.getPassword() + item.getSalt()));
        }

        return updateById(item);
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
        String codeStr = (String) request.getSession().getAttribute(Const.SESSION_CODE);
        if (!dto.getCode().equalsIgnoreCase(codeStr)) {
            throw new ApiException(ApiResultEnum.SYSTEM_CODE_ERROR);
        }
        SysUser sysUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername()));
        Optional.ofNullable(sysUser).orElseThrow(() -> new ApiException(ApiResultEnum.SYSTEM_ACCOUNT_NOT_FOUND));
        String salt = sysUser.getSalt();
        String pwd = SecureUtil.md5(dto.getPassword() + salt);
        if (!pwd.equals(sysUser.getPassword())) {
            throw new ApiException(ApiResultEnum.SYSTEM_PASSWORD_ERROR);
        }
        // 登录成功
        StpUtil.login(sysUser.getId());
        SaSession session = StpUtil.getSession();
        session.set(Const.SESSION_USER,sysUser);
        // 数据脱敏
        sysUser.setPassword("****");
        sysUser.setSalt("****");
        return sysUser;
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
                .filter(i -> i.getMenuType().equals(1))
                .map(this::convertToTabsVo)
                .collect(Collectors.toList());
        tabsVo.setChildren(child);
        return tabsVo;
    }
}
