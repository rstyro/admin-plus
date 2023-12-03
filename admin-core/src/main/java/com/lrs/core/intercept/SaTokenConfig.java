package com.lrs.core.intercept;


import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.thymeleaf.dialect.SaTokenDialect;
import com.lrs.core.system.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 登录拦截
 */
@Slf4j
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Resource
    private CommonConfig.SecurityConfig securityConfig;

    @Bean
    public LoginIntercept loginIntercept() {
        return new LoginIntercept();
    }

    // Sa-Token 标签方言 (Thymeleaf版)
    @Bean
    public SaTokenDialect getSaTokenDialect() {
        return new SaTokenDialect();
    }

    @Bean
    public ContextInterceptor contextInterceptor(){
        return new ContextInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginIntercept())
                .addPathPatterns("/**")
                .excludePathPatterns(securityConfig.getExcludes());
        registry.addInterceptor(contextInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(securityConfig.getExcludes());
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(securityConfig.getExcludes());
    }
}
