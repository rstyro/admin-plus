package com.lrs.core.controller;

import com.lrs.core.config.CommonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 自定义错误页面
 */
@Controller
public class IndexController {

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private CommonConfig.UploadConfig uploadConfig;

    @GetMapping("/login")
    public String index(){
        System.out.println("index-name="+commonConfig.getName());
        System.out.println("index-root="+uploadConfig.getRoot());
        System.out.println("index-pre="+uploadConfig.getPre());
        return "login";
    }
}
