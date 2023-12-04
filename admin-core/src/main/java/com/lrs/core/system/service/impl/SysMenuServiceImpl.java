package com.lrs.core.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.core.system.SystemConst;
import com.lrs.core.system.dto.MenuDto;
import com.lrs.core.system.entity.SysBtn;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.mapper.SysMenuMapper;
import com.lrs.core.system.service.ISysBtnService;
import com.lrs.core.system.service.ISysMenuService;
import com.lrs.core.system.vo.RoleMenuTreeVo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Iterator;
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

    @Resource
    private ISysBtnService sysBtnService;

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

    /**
     * 分页获取菜单列表
     * @param page 分页
     * @param menuDto 参数
     * @return 分页列表
     */
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
        addMenuBtn(sysMenu);
        return save;
    }

    @Override
    public boolean edit(SysMenu sysMenu) {
        addMenuBtn(sysMenu);
        return updateById(sysMenu);
    }

    public void addMenuBtn(SysMenu sysMenu){
        if (sysMenu.isAddBtn() && sysMenu.getMenuType().equals(SystemConst.MenuType.MENU)) {
            //todo 添加该菜单的公用按钮权限
            List<SysBtn> list = sysBtnService.list();
            List<String> btnNameList = this.list(new LambdaQueryWrapper<SysMenu>()
                    .eq(SysMenu::getParentId, sysMenu.getId()))
                    .stream()
                    .map(SysMenu::getMenuName)
                    .collect(Collectors.toList());

            List<SysBtn> notExistList = list.stream().filter(i -> !btnNameList.contains(i.getBtnKey())).collect(Collectors.toList());
            List<SysMenu> addList = notExistList.stream().map(i -> {
                SysMenu btnMenu = new SysMenu();
                btnMenu.setMenuName(i.getBtnName());
                btnMenu.setMenuType(SystemConst.MenuType.BTN);
                btnMenu.setParentId(sysMenu.getId());
                btnMenu.setPermit(sysMenu.getPermit() + ":" + i.getBtnKey());
                return btnMenu;
            }).collect(Collectors.toList());
            if(addList.size()>0){
                this.saveBatch(addList);
            }
        }
    }

    @Override
    public boolean del(Long id) {
        boolean suc = removeById(id);
        if(suc){
            // 删除子级菜单
            remove(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId,id));
        }
        return suc;
    }

    @Override
    public boolean batchDel(List<Long> ids) {
        boolean suc = removeBatchByIds(ids);
        if(suc){
            // 删除子级菜单
            remove(new LambdaQueryWrapper<SysMenu>().in(SysMenu::getParentId,ids));
        }
        return suc;
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
