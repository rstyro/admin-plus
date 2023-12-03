package com.lrs.core.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.core.system.dto.SysRoleDto;
import com.lrs.core.system.dto.SysUserDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.entity.SysRole;
import com.lrs.core.system.entity.SysRoleMenu;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.mapper.SysRoleMapper;
import com.lrs.core.system.service.ISysRoleMenuService;
import com.lrs.core.system.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
