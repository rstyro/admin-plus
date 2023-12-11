package com.lrs.core.system.controller;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrs.common.annotation.Log;
import com.lrs.common.constant.BusinessType;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysBtn;
import com.lrs.core.system.service.ISysBtnService;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/system/btn")
public class SysBtnController extends BaseController {

    @Resource
    private ISysBtnService sysBtnService;

    /**
     * 页面跳转
     */
    @SaCheckPermission(value = {"system:btn:list","system:btn:list:view"},mode = SaMode.OR)
    @GetMapping("/page")
    public String page() {
        return "system/btn";
    }

    /**
     * 列表页
     */
    @SaCheckPermission(value = {"system:btn:list","system:btn:list:view"},mode = SaMode.OR)
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody BaseDto dto) {
        Page<SysBtn> menuPage = sysBtnService.getPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
     * 添加
     */
    @Log(title = "按钮管理",businessType = BusinessType.INSERT)
    @SaCheckPermission("system:btn:list:add")
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody SysBtn item) {
        return R.ok(sysBtnService.add(item));
    }

    /**
     * 编辑
     */
    @Log(title = "按钮管理",businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @SaCheckPermission("system:btn:list:edit")
    @ResponseBody
    public R edit(@RequestBody SysBtn item) {
        return R.ok(sysBtnService.edit(item));
    }

    /**
     * 删除
     */
    @Log(title = "按钮管理",businessType = BusinessType.DELETE)
    @GetMapping("/del")
    @SaCheckPermission("system:btn:list:del")
    @ResponseBody
    public R del(Long id) {
        return R.ok(sysBtnService.del(id));
    }

    /**
     * 批量删除
     */
    @Log(title = "按钮管理",businessType = BusinessType.DELETE)
    @PostMapping("/batchDel")
    @SaCheckPermission("system:btn:list:del")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids) {
        return R.ok(sysBtnService.batchDel(ids));
    }
}
