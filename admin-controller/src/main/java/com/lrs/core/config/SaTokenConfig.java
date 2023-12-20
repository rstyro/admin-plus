package com.lrs.core.config;

import cn.dev33.satoken.thymeleaf.dialect.SaTokenDialect;
import com.lrs.common.utils.RedisSimulation;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SaTokenConfig {
    // Sa-Token 标签方言 (Thymeleaf版)
    @Bean
    public SaTokenDialect getSaTokenDialect() {
        return new SaTokenDialect();
    }

    // Redis 模拟器
    @Bean
    public RedisSimulation redisSimulation() {
        return new RedisSimulation();
    }

}
