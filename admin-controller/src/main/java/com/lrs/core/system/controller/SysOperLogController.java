package com.lrs.core.system.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.system.entity.SysOperLog;
import com.lrs.core.system.service.ISysOperLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 *  操作日志记录 前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2023-12-11
 */
@Controller
@RequestMapping("/system/sysOperLog")
public class SysOperLogController extends BaseController {

    @Resource
    private ISysOperLogService sysOperLogService;


    /**
    * 页面跳转
    */
    @SaCheckPermission(value = {"system:sysOperLog:list","system:sysOperLog:list:view"},mode = SaMode.OR)
    @GetMapping("/page")
    public String page() {
        return "page/system/sysOperLog_list";
    }

    /**
    * 列表页
    */
    @SaCheckPermission(value = {"system:sysOperLog:list","system:sysOperLog:list:view"},mode = SaMode.OR)
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody BaseDto dto) {
    Page<SysOperLog> menuPage = sysOperLogService.getPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

}