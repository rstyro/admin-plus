package com.lrs.core.intercept;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
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
        String servletPath = request.getServletPath();
        //  如果方法有@SaIgnore 也直接返回true
        if (hasSaIgnoreAnnotation(handler)) {
            log.info("@SaIgnore-忽略认证路径，url={}", servletPath);
            return true;
        }
        if(StpUtil.isLogin()){
            return true;
        }
        response.sendRedirect(request.getContextPath()+"/toLogin");
        log.info("{} 重定向登录页",servletPath);
        return false;
    }

    /**
     * 检查处理器是否有@SaIgnore注解（类级别或方法级别）
     */
    private boolean hasSaIgnoreAnnotation(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 检查方法上的注解
        SaIgnore methodAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), SaIgnore.class);
        if (methodAnnotation != null) {
            return true;
        }
        // 检查类上的注解
        SaIgnore classAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), SaIgnore.class);
        return classAnnotation != null;
    }
}
