package com.lrs.core.${package.ModuleName}.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.R;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.dto.BaseDto;
import com.lrs.core.${package.ModuleName}.entity.${table.entityName};
import com.lrs.core.${package.ModuleName}.service.${table.serviceName};
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 *  ${table.comment!} 前端控制器
 * </p>
 *
 * @author rstyro
 * @since ${.now?date}
 */
@Controller
@RequestMapping("/${package.ModuleName}/${table.entityPath}")
public class ${table.controllerName} extends BaseController {

    @Resource
    private ${table.serviceName} ${table.entityPath}Service;


    /**
    * 页面跳转
    */
    @SaCheckPermission(value = {"${package.ModuleName}:${table.entityPath}:list","${package.ModuleName}:${table.entityPath}:list:view"},mode = SaMode.OR)
    @GetMapping("/page")
    public String page() {
        return "page/${package.ModuleName}/${table.entityPath}_list";
    }

    /**
    * 列表页
    */
    @SaCheckPermission(value = {"${package.ModuleName}:${table.entityPath}:list","${package.ModuleName}:${table.entityPath}:list:view"},mode = SaMode.OR)
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody BaseDto dto) {
    Page<${table.entityName}> menuPage = ${table.entityPath}Service.getPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
    * 添加
    */
    @SaCheckPermission("${package.ModuleName}:${table.entityPath}:list:add")
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody ${table.entityName} item) {
        return R.ok(${table.entityPath}Service.add(item));
    }


    /**
    * 编辑
    */
    @SaCheckPermission("${package.ModuleName}:${table.entityPath}:list:edit")
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody ${table.entityName} item) {
        return R.ok(${table.entityPath}Service.edit(item));
    }

    /**
    * 删除
    */
    @SaCheckPermission("${package.ModuleName}:${table.entityPath}:list:del")
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id) {
        return R.ok(${table.entityPath}Service.del(id));
    }

    /**
    * 批量删除
    */
    @SaCheckPermission("${package.ModuleName}:${table.entityPath}:list:del")
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids) {
        return R.ok(${table.entityPath}Service.batchDel(ids));
    }

}