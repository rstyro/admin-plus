package com.lrs.core.config;

import javax.servlet.MultipartConfigElement;

import com.lrs.common.utils.PathsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {

	@Value("${admin.upload.root}")
	public String rootPath;
	
	/**
	 * 这个bean是为了自定义上传路径
	 * @return
	 */
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(PathsUtils.getAbsolutePath(""));
        return factory.createMultipartConfig();
    }
}
