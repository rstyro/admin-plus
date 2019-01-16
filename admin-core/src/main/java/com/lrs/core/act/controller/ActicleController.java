package com.lrs.core.act.controller;


import com.lrs.common.annotation.Permission;
import com.lrs.common.annotation.PermissionType;
import com.lrs.core.act.entity.Acticle;
import com.lrs.core.act.entity.ActicleDTO;
import com.lrs.core.act.service.IActicleService;
import com.lrs.core.admin.entity.Menu;
import com.lrs.core.admin.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lrs.core.base.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2019-01-16
 */
@RestController
@RequestMapping("/act/acticle")
public class ActicleController extends BaseController {

    private final static String qxurl = "act/acticle/list";

    @Autowired
    private IActicleService acticleService;

    @GetMapping("/list")
    public String list(Model model, ActicleDTO dto) throws Exception {
        model.addAttribute("list",acticleService.getList(dto));
        return "page/act/list";
    }

    @PostMapping(value="/add")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.ADD)
    public Object add(Acticle item) throws Exception {
        return acticleService.add(item,this.getSession());
    }

}
