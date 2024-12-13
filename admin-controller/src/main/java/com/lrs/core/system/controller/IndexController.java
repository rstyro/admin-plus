package com.lrs.core.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import com.lrs.common.constant.Const;
import com.lrs.common.vo.TabsVo;
import com.lrs.core.config.CommonConfig;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页相关
 */
@Slf4j
@Controller
public class IndexController {

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private ISysUserService sysUserService;

    /**
     * 登录页
     */
    @GetMapping(value = {"/", "/toLogin"})
    public String index() {
        return "login";
    }

    /**
     * 首页
     * @param model model
     */
    @GetMapping("/index")
    public String index(Model model) {
        SysUser sysUser = Convert.convert(SysUser.class, StpUtil.getSession().get(Const.SessionKey.SESSION_USER));
        model.addAttribute("systemName", commonConfig.getSystemName());
        model.addAttribute("user", sysUser);
        // tabs菜单
        List<TabsVo> tabMenuList = sysUserService.getTabMenuList(StpUtil.getLoginId(0l));
        model.addAttribute("tabs", tabMenuList);
        return "index";
    }

    /**
     * 欢迎页
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * icon
     */
    @GetMapping("/icon")
    public String icon() {
        return "icon";
    }

    /**
     * tinymce Demo 页面
     */
    @GetMapping("/tinymceDemo")
    public String tinymceDemo() {
        return "page/tinymce/tinymceDemo";
    }


}
