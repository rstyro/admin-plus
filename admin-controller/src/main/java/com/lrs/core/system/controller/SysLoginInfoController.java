package com.lrs.core.system.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysLoginInfo;
import com.lrs.core.system.service.ISysLoginInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 *  系统访问记录 前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2023-12-12
 */
@Controller
@RequestMapping("/system/sysLoginInfo")
public class SysLoginInfoController extends BaseController {

    @Resource
    private ISysLoginInfoService sysLoginInfoService;


    /**
    * 页面跳转
    */
    @SaCheckPermission(value = {"system:sysLoginInfo:list","system:sysLoginInfo:list:view"},mode = SaMode.OR)
    @GetMapping("/page")
    public String page() {
        return "page/system/sysLoginInfo_list";
    }

    /**
    * 列表页
    */
    @SaCheckPermission(value = {"system:sysLoginInfo:list","system:sysLoginInfo:list:view"},mode = SaMode.OR)
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody BaseDto dto) {
    Page<SysLoginInfo> menuPage = sysLoginInfoService.getPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
    * 添加
    */
    @SaCheckPermission("system:sysLoginInfo:list:add")
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody SysLoginInfo item) {
        return R.ok(sysLoginInfoService.add(item));
    }


    /**
    * 编辑
    */
    @SaCheckPermission("system:sysLoginInfo:list:edit")
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody SysLoginInfo item) {
        return R.ok(sysLoginInfoService.edit(item));
    }

    /**
    * 删除
    */
    @SaCheckPermission("system:sysLoginInfo:list:del")
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id) {
        return R.ok(sysLoginInfoService.del(id));
    }

    /**
    * 批量删除
    */
    @SaCheckPermission("system:sysLoginInfo:list:del")
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids) {
        return R.ok(sysLoginInfoService.batchDel(ids));
    }

}