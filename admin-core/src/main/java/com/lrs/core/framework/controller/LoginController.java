package com.lrs.core.framework.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.lrs.common.constant.R;
import com.lrs.core.framework.config.CommonConfig;
import com.lrs.core.framework.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 自定义错误页面
 */
@Controller
public class LoginController {

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private CommonConfig.UploadConfig uploadConfig;

    /**
     * 登录页
     */
    @GetMapping(value = {"/","/toLogin"})
    public String index(){
        System.out.println("index-name="+commonConfig.getName());
        System.out.println("index-root="+uploadConfig.getRoot());
        System.out.println("index-pre="+uploadConfig.getPre());
        return "login";
    }

    /**
     * 首页
     * @param model model
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/index")
    public String index(Model model){

        return "index";
    }

    /**
     * 用户登录
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(@RequestBody LoginDto dto) throws Exception {
        StpUtil.login(1);
        return R.ok();
    }

    /**
     * 用户注销
     * @return
     */
    @PostMapping("/logout")
    public R logout() throws Exception {
        StpUtil.logout();
        return R.ok();
    }
}
