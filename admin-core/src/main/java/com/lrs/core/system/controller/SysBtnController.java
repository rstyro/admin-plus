package com.lrs.core.system.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    @GetMapping("/page")
    public String page() {
        return "system/btn";
    }

    /**
     * 列表页
     */
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody BaseDto dto) {
        Page<SysBtn> menuPage = sysBtnService.getPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody SysBtn item) {
        return R.ok(sysBtnService.add(item));
    }

    /**
     * 编辑
     */
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody SysBtn item) {
        return R.ok(sysBtnService.edit(item));
    }

    /**
     * 删除
     */
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id) {
        return R.ok(sysBtnService.del(id));
    }

    /**
     * 批量删除
     */
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids) {
        return R.ok(sysBtnService.batchDel(ids));
    }
}
