package com.lrs.core.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.core.system.dto.SysRoleDto;
import com.lrs.core.system.entity.SysRole;
import com.lrs.core.system.entity.SysRoleMenu;
import com.lrs.core.system.entity.SysUserRole;
import com.lrs.core.system.mapper.SysRoleMapper;
import com.lrs.core.system.service.ISysRoleMenuService;
import com.lrs.core.system.service.ISysRoleService;
import com.lrs.core.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private ISysRoleMenuService sysRoleMenuService;

    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Override
    public Page<SysRole> getRolePage(Page page, SysRoleDto dto) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            queryWrapper.like(SysRole::getRoleName, dto.getKeyword());
        }
        queryWrapper.orderByAsc(SysRole::getId);
        return page(page, queryWrapper);
    }

    @Override
    public boolean add(SysRole item) {
        boolean save = save(item);
        if(save){
            addRoleMenu(item);
        }
        return save;
    }

    @Override
    public boolean edit(SysRole item) {
        addRoleMenu(item);
        return updateById(item);
    }

    public void addRoleMenu(SysRole item){
        // 清空原来的权限，然后添加新的权限
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, item.getId()));
        List<Long> menuIdList = Optional.ofNullable(item.getMenuIdList()).orElse(new ArrayList<>());
        List<SysRoleMenu> addList = menuIdList.stream().map(i -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(Long.valueOf(item.getId()));
            sysRoleMenu.setMenuId(i);
            return sysRoleMenu;
        }).collect(Collectors.toList());
        if(addList.size()>0){
            sysRoleMenuService.saveBatch(addList);
        }
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
    public List<SysRole> getUserRoleList(Long userId) {
        List<SysRole> list = list();
        List<Integer> hasRoleIdList = sysUserRoleService.list(new LambdaUpdateWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        for (SysRole sysRole : list) {
            sysRole.setHasRole(hasRoleIdList.contains(sysRole.getId()));
        }
        return list;
    }

    @Override
    public boolean editUserRole(SysRoleDto dto) {
        // 清空角色后添加
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,dto.getUserId()));
        List<Integer> roleIdList = Optional.ofNullable(dto.getRoleIdList()).orElse(new ArrayList<>());
        List<SysUserRole> addList = roleIdList.stream().map(i -> {
            SysUserRole item = new SysUserRole();
            item.setRoleId(i);
            item.setUserId(dto.getUserId());
            return item;
        }).collect(Collectors.toList());
        if(addList.size()>0){
            sysUserRoleService.saveBatch(addList);
        }
        return false;
    }
}
