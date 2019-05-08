package com.lrs.core.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.ResponseModel;
import com.lrs.common.constant.Result;
import com.lrs.common.exception.ApiException;
import com.lrs.core.admin.entity.Menu;
import com.lrs.core.admin.mapper.MenuMapper;
import com.lrs.core.admin.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public long getMaxId() {
        return menuMapper.getMaxMenuId();
    }

    @Override
    public List<Menu> getAllMenuList() {
        List<Menu> parentMenuList =this.getAllParentMenuList();
        for(Menu menu:parentMenuList){
            List<Menu> subMenuList = getSubMenuListByParentId(menu.getMenuId());
            if(subMenuList == null){
                subMenuList = new ArrayList<>();
            }
            menu.setSubMenu(subMenuList);
        }
        return parentMenuList;
    }

    @Override
    public List<Menu> getAllParentMenuList() {
        return this.list(new QueryWrapper<Menu>().lambda().eq(Menu::getIsDel,0).eq(Menu::getParentId,0));
    }

    @Override
    public List<Menu> getSubMenuListByParentId(Integer parentId) {
        return this.list(new QueryWrapper<Menu>().lambda().eq(Menu::getIsDel,0).eq(Menu::getParentId,parentId));
    }

    @Override
    public Result getSubMenuList(Integer parentId) {
        if(parentId == null) throw  new ApiException(ApiResultEnum.PARAMETER_NULL,null);
        return Result.ok(this.getSubMenuListByParentId(parentId));
    }

    @Override
    public Result addMenu(Menu menu, HttpSession session) {
        this.save(menu);
        return Result.ok();
    }

    @Override
    public Result delMenu(Integer menuId) {
        Menu menu = new Menu();
        menu.setMenuId(menuId);
        menu.setIsDel(1);
        this.updateById(menu);
        return Result.ok();
    }

    @Override
    public Result editMenu(Menu menu) {
        this.updateById(menu);
        return Result.ok();
    }

    @Override
    public Result findMenu(Integer menuId) {
        return Result.ok(this.getOne(new QueryWrapper<Menu>().lambda().eq(Menu::getMenuId,menuId)));
    }
}
