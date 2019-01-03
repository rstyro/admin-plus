package com.lrs.core.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lrs.core.entity.ApiResultEnum;
import com.lrs.core.entity.ResponseModel;
import com.lrs.core.exception.ApiException;
import com.lrs.core.sys.entity.Menu;
import com.lrs.core.sys.mapper.MenuMapper;
import com.lrs.core.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    public ResponseModel getSubMenuList(Integer parentId) {
        if(parentId == null) throw  new ApiException(ApiResultEnum.PARAMETER_NULL,null);
        return new ResponseModel(this.getSubMenuListByParentId(parentId));
    }

    @Override
    public ResponseModel addMenu(Menu menu, HttpSession session) {
        this.save(menu);
        return new ResponseModel("");
    }

    @Override
    public ResponseModel delMenu(Integer menuId) {
        Menu menu = new Menu();
        menu.setMenuId(menuId);
        menu.setIsDel(1);
        this.updateById(menu);
        return new ResponseModel("");
    }

    @Override
    public ResponseModel editMenu(Menu menu) {
        this.updateById(menu);
        return new ResponseModel("");
    }

    @Override
    public ResponseModel findMenu(Integer menuId) {
        return new ResponseModel(this.getOne(new QueryWrapper<Menu>().lambda().eq(Menu::getMenuId,menuId)));
    }
}
