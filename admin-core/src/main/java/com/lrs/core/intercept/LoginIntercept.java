package com.lrs.core.intercept;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginIntercept implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(StpUtil.isLogin()){
            return true;
        }
        response.sendRedirect("/toLogin");
        log.info("{} 重定向登录页",request.getServletPath());
        return false;
    }
}
