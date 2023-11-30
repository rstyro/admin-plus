package com.lrs.core.framework.controller;

import com.lrs.core.framework.config.CommonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * 自定义错误页面
 */
@Controller
public class ErrorPageController {

    @Resource
    private CommonConfig commonConfig;

    @GetMapping("/error/{pageNumber}")
    public String error(@PathVariable("pageNumber") String pageNumber){
        System.out.println("name="+commonConfig.getName());
        return "error/"+pageNumber;
    }
}
