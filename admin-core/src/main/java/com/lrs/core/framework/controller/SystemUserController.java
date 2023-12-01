package com.lrs.core.framework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 自定义错误页面
 */
@Slf4j
@Controller
@RequestMapping("/system/user")
public class SystemUserController {

    /**
     * 用户列表页
     */
    @GetMapping("/list")
    public String list(){
        return "system/user";
    }

}
