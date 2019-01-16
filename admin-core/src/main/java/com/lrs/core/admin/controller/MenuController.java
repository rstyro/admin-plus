package com.lrs.core.admin.controller;


import com.lrs.common.annotation.Permission;
import com.lrs.common.annotation.PermissionType;
import com.lrs.core.base.BaseController;
import com.lrs.core.admin.entity.Menu;
import com.lrs.core.admin.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Controller
@RequestMapping("/admin/menu")
public class MenuController extends BaseController {

    private final static String qxurl = "admin/menu/list";

    @Autowired
    private IMenuService menuService;

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("menus",menuService.getAllParentMenuList());
        return "page/admin/menu/list";
    }

    @PostMapping(value="/add")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.ADD)
    public Object add(Menu menu){
        return menuService.addMenu(menu,this.getSession());
    }

    @GetMapping(value="/del/{menu_id}")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.DEL)
    public Object del(@PathVariable("menu_id") Integer menuId){
        return menuService.delMenu(menuId);
    }

    @PostMapping(value="/edit")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.EDIT)
    public Object edit(Menu menu){
        return menuService.editMenu(menu);
    }

    @GetMapping(value="/query/{menu_id}")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.QUERY)
    public Object find(@PathVariable("menu_id") Integer menuId){
        return menuService.findMenu(menuId);
    }

    @GetMapping("/getSubMenu")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.QUERY)
    public Object getSubMenu(Integer parentId){
        return menuService.getSubMenuList(parentId);
    }
}
