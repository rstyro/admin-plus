package com.lrs.core.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.lrs.common.constant.Const;
import com.lrs.common.vo.R;
import com.lrs.common.vo.TabsVo;
import com.lrs.core.config.CommonConfig;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
//        SysUser sysUser = Convert.convert(SysUser.class, StpUtil.getSession().get(Const.SessionKey.SESSION_USER));
        SysUser sysUser = JSON.parseObject(JSON.toJSONString(StpUtil.getSession().get(Const.SessionKey.SESSION_USER)),SysUser.class);

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

    /**
     * 获取当前登录用户的 Tab 菜单树
     * @description 前后端分离后供前端生成动态路由与侧栏菜单（替代旧版 Thymeleaf ${tabs}）
     * @return R<List<TabsVo>> 菜单树
     */
    @GetMapping("/menu/tabList")
    @ResponseBody
    public R<List<TabsVo>> tabList() {
        return R.ok(sysUserService.getTabMenuList(StpUtil.getLoginId(0L)));
    }


}
