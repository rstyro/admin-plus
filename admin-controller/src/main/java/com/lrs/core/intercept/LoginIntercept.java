package com.lrs.core.intercept;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 登录拦截，未登录重定向到登陆页面
 */
@Slf4j
public class LoginIntercept implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是 Spring MVC 的控制器方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //  检查方法或类上是否标注了 @SaIgnore
        SaIgnore saIgnore = handlerMethod.getMethodAnnotation(SaIgnore.class);
        if (saIgnore == null) {
            saIgnore = handlerMethod.getBeanType().getAnnotation(SaIgnore.class);
        }

        // 3. 如果存在 @SaIgnore 注解，直接放行，不再执行登录检查
        if (saIgnore != null) {
            log.info("请求 {} 被 @SaIgnore 放行", request.getServletPath());
            return true;
        }

        if(StpUtil.isLogin()){
            return true;
        }
        response.sendRedirect(request.getContextPath()+"/toLogin");
        log.info("{} 重定向登录页",request.getServletPath());
        return false;
    }
}
