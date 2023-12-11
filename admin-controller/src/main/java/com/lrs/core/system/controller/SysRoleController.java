package com.lrs.core.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.common.annotation.Log;
import com.lrs.common.constant.BusinessType;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.SysRoleDto;
import com.lrs.core.system.entity.SysRole;
import com.lrs.core.system.service.ISysRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {


    @Resource
    private ISysRoleService sysRoleService;

    /**
     * 页面跳转
     */
    @SaCheckPermission(value = {"system:role:list","system:role:list:view"},mode = SaMode.OR)
    @GetMapping("/page")
    public String page() {
        return "system/role";
    }

    /**
     * 列表页
     */
    @SaCheckPermission(value = {"system:role:list","system:role:list:view"},mode = SaMode.OR)
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody SysRoleDto dto) {
        Page<SysRole> menuPage = sysRoleService.getRolePage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
     * 添加
     */
    @Log(title = "角色管理",businessType = BusinessType.INSERT)
    @SaCheckPermission("system:role:list:add")
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody SysRole item) {
        return R.ok(sysRoleService.add(item));
    }

    /**
     * 编辑
     */
    @Log(title = "角色管理",businessType = BusinessType.UPDATE)
    @SaCheckPermission("system:role:list:edit")
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody SysRole item) {
        return R.ok(sysRoleService.edit(item));
    }

    /**
     * 删除
     */
    @Log(title = "角色管理",businessType = BusinessType.DELETE)
    @SaCheckPermission("system:role:list:del")
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id) {
        System.out.println(id);
        return R.ok(sysRoleService.del(id));
    }

    /**
     * 批量删除
     */
    @Log(title = "角色管理",businessType = BusinessType.DELETE)
    @SaCheckPermission("system:role:list:del")
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids) {
        return R.ok(sysRoleService.batchDel(ids));
    }

    /**
     * 获取用户角色
     */
    @GetMapping("/getUserRole")
    @ResponseBody
    public R getUserRole(Long userId) {
        return R.ok(sysRoleService.getUserRoleList(userId));
    }

    /**
     * 编辑用户角色
     */
    @PostMapping("/editUserRole")
    @ResponseBody
    public R editUserRole(@RequestBody SysRoleDto dto) {
        return R.ok(sysRoleService.editUserRole(dto));
    }
}
