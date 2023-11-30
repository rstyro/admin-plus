package com.lrs.core.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@ConfigurationProperties(prefix = "admin.common")
@Configuration
@Data
public class CommonConfig {
    private String name;

    @ConfigurationProperties(prefix = "admin.common.upload")
    @Configuration
    @Data
    public class UploadConfig {
        private String root;
        private String pre;

    }

    @ConfigurationProperties(prefix = "security")
    @Configuration
    @Data
    public class SecurityConfig {
        private List<String> excludes;

    }
}
