package com.lrs.core.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.MenuDto;
import com.lrs.core.system.dto.SysUserDto;
import com.lrs.core.system.entity.SysMenu;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.service.ISysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    @Resource
    private ISysUserService sysUserService;

    /**
     * 列表页
     */
    @GetMapping("/page")
    public String page() {
        return "system/user";
    }

    /**
     * 列表页
     */
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody SysUserDto dto) {
        Page<SysUser> menuPage = sysUserService.getUserPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody SysUser item) {
        return R.ok(sysUserService.add(item));
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody SysUser item) {
        return R.ok(sysUserService.edit(item));
    }

    /**
     * 删除
     */
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id) {
        System.out.println(id);
        return R.ok(sysUserService.del(id));
    }

    /**
     * 批量删除
     */
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids) {
        return R.ok(sysUserService.batchDel(ids));
    }
}
