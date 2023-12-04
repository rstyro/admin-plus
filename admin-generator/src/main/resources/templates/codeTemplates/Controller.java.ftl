package com.lrs.core.${package.ModuleName}.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    @GetMapping("/page")
    public String page() {
        return "page/${package.ModuleName}/${table.entityPath}_list";
    }

    /**
    * 列表页
    */
    @PostMapping("/list")
    @ResponseBody
    public R list(@RequestBody BaseDto dto) {
    Page<${table.entityName}> menuPage = ${table.entityPath}Service.getPage(new Page<>(ContextUtil.getPageNo(), ContextUtil.getPageSize()), dto);
        return R.ok(menuPage);
    }

    /**
    * 添加
    */
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestBody ${table.entityName} item) {
        return R.ok(${table.entityPath}Service.add(item));
    }


    /**
    * 编辑
    */
    @PostMapping("/edit")
    @ResponseBody
    public R edit(@RequestBody ${table.entityName} item) {
        return R.ok(${table.entityPath}Service.edit(item));
    }

    /**
    * 删除
    */
    @GetMapping("/del")
    @ResponseBody
    public R del(Long id) {
        return R.ok(${table.entityPath}Service.del(id));
    }

    /**
    * 批量删除
    */
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestBody List<Long> ids) {
        return R.ok(${table.entityPath}Service.batchDel(ids));
    }

}