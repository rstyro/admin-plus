package com.lrs.core.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.SysRoleDto;
import com.lrs.core.system.dto.SysUserDto;
import com.lrs.core.system.entity.SysRole;
import com.lrs.core.system.entity.SysUser;
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
    @GetMapping("/page")
    public String page() {
        return "system/role";
    }

    /**
     * 列表页
     */
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody SysRoleDto dto) {
        Page<SysRole> menuPage = sysRoleService.getRolePage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody SysRole item) {
        return R.ok(sysRoleService.add(item));
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody SysRole item) {
        return R.ok(sysRoleService.edit(item));
    }

    /**
     * 删除
     */
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id) {
        System.out.println(id);
        return R.ok(sysRoleService.del(id));
    }

    /**
     * 批量删除
     */
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
