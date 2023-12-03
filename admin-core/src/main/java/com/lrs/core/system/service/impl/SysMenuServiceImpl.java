package com.lrs.core.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.core.system.dto.MenuDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.mapper.SysMenuMapper;
import com.lrs.core.system.service.ISysMenuService;
import com.lrs.core.system.vo.RoleMenuTreeVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
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
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> getMenuListByParentId(Long parentId) {
        return list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId)
                .orderByAsc(SysMenu::getSortNum));
    }

    @Override
    public List<SysMenu> getAllMenuList() {
        List<SysMenu> list = getMenuListByParentId(0l);
        return buildMenuTree(list);
    }

    /**
     * 获取角色 的菜单列表
     *
     * @return
     */
    @Override
    public List<RoleMenuTreeVo> getAllRoleMenuList(Long roleId) {
        List<RoleMenuTreeVo> roleMenuList = baseMapper.getRoleMenuList(roleId, 0l);
        buildRoleMenuTree(roleMenuList,roleId);
        return roleMenuList;
    }

    @Override
    public Page<SysMenu> getMenuPage(Page page, MenuDto menuDto) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, 0);
        if (!ObjectUtils.isEmpty(menuDto.getKeyword())) {
            queryWrapper.like(SysMenu::getMenuName, menuDto.getKeyword());
        }
        queryWrapper.orderByAsc(SysMenu::getSortNum);
        Page<SysMenu> pageResult = page(page, queryWrapper);
        buildMenuTree(pageResult.getRecords());
        return pageResult;
    }

    @Override
    public boolean add(SysMenu sysMenu) {
        boolean save = save(sysMenu);
        if (sysMenu.isAddBtn()) {
            //todo 添加该菜单的公用按钮权限

        }
        return save;
    }

    @Override
    public boolean edit(SysMenu sysMenu) {
        return updateById(sysMenu);
    }

    @Override
    public boolean del(Long id) {
        return removeById(id);
    }

    @Override
    public boolean batchDel(List<Long> ids) {
        return removeBatchByIds(ids);
    }

    private List<SysMenu> buildMenuTree(List<SysMenu> list) {
        return list.stream()
                .peek(m -> m.setChild(buildMenuTree(getMenuListByParentId(m.getId()))))
                .collect(Collectors.toList());
    }

    private List<RoleMenuTreeVo> buildRoleMenuTree(List<RoleMenuTreeVo> list, Long roleId) {
        return list.stream()
                .peek(m -> m.setChild(buildRoleMenuTree(baseMapper.getRoleMenuList(roleId, m.getId()), roleId)))
                .collect(Collectors.toList());
    }

}
