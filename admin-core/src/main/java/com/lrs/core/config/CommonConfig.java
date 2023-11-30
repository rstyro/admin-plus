package com.lrs.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


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
}
