package com.lrs.core;

import cn.hutool.core.net.NetUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

@MapperScan({"com.lrs.core.*.mapper"})
@ServletComponentScan
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

	public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
        Environment env = application.getEnvironment();
        String ip = NetUtil.getLocalhostStr();
        String port = env.getProperty("server.port");
        String property = env.getProperty("server.servlet.context-path");
        String path = property == null ? "" :  property;
        System.out.println(
                "\n\t" +
                        "----------------------------------------------------------\n\t" +
                        "Application admin-plus is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                        "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                        "------------------------------------------------------------");
	}

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 自定义错误页面
        factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
        factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
    }
}
