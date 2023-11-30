package com.lrs.core.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 自定义错误页面
 */
@Controller
public class ErrorPageController {

    @GetMapping("/error/{pageNumber}")
    public String error(@PathVariable("pageNumber") String pageNumber){
        return "error/"+pageNumber;
    }
}
