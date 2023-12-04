package com.lrs.core.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.MenuDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.service.ISysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
@Controller
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    @Resource
    private ISysMenuService menuService;

    /**
     * 列表页
     */
    @SaCheckPermission("system:menu:list")
    @GetMapping("/page")
    public String page(){
        return "system/menu";
    }

    /**
     * 列表页
     */
    @SaCheckPermission("system:menu:list")
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody MenuDto dto){
        Page<SysMenu> menuPage = menuService.getMenuPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
     * 获取角色的菜单列表
     */
    @GetMapping("/getRoleMenuList")
    @ResponseBody
    public R list(Long roleId){
        return R.ok(menuService.getAllRoleMenuList(roleId));
    }

    /**
     * 添加
     */
    @SaCheckPermission("system:menu:list:add")
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody SysMenu sysMenu){
        return R.ok(menuService.add(sysMenu));
    }
    /**
     * 编辑
     */
    @SaCheckPermission("system:menu:list:edit")
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody SysMenu sysMenu){
        return R.ok(menuService.edit(sysMenu));
    }
    /**
     * 删除
     */
    @SaCheckPermission("system:menu:list:del")
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id){
        System.out.println(id);
        return R.ok(menuService.del(id));
    }
    /**
     * 批量删除
     */
    @SaCheckPermission("system:menu:list:del")
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids){
        System.out.println(ids);
        return R.ok(menuService.batchDel(ids));
    }

}
