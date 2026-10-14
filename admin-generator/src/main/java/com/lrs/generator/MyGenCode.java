package com.lrs.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.lrs.generator.base.BaseGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyGenCode extends BaseGenerator {

    public static void main(String[] args) {
        Map<String,Object> param = new HashMap<>();
        param.put("custName","测试注入参数");

        // 代码生成位置
        String outPath = System.getProperty("user.dir")+"/admin-generator/";
        // 仓库根目录：user.dir 在 IDE 里可能是仓库根，命令行 mvn -pl 下是 admin-generator
        java.io.File rootDir = new java.io.File(outPath);
        if (outPath.endsWith("admin-generator/")) {
            rootDir = rootDir.getParentFile();
        }
        String projectRoot = rootDir.getAbsolutePath().replace('\\', '/');
        AutoGenerator autoGenerator =  new AutoGenerator(getDataSourceConfig().build());
        //配置相关
        GlobalConfig globalConfig = getGlobalConfig(outPath).build();
        PackageConfig packageConfig = getPackageConfig(outPath,scannerNext("输入模块名（如果需要生成页面模块要确定）：")).build();
        param.put("basePackage", packageConfig.getParent());
        String result = scannerNext("生成哪种页面？0=仅后端代码，1=旧版页面(Vue2+Thymeleaf，对照用)，2=新版web页面(Vue3，同时生成菜单SQL)");
        List<String> tables = getTables(scannerNext("请输入表名，多个英文逗号分隔？所有输入 all"));
        StrategyConfig strategyConfig=null;
        int mode = Integer.parseInt(result.trim());
        if(mode != 0){
            strategyConfig = getStrategyConfig(tables).build();

            InjectionConfig injectionConfig = getInjectionConfig(param, mode, projectRoot, packageConfig.getModuleName()).build();
            // 注入配置，可生成自定义 页面、VO 、DTO 等
            autoGenerator.injection(injectionConfig);
        }else {
            strategyConfig = getDefaultStrategyConfig(tables).build();
        }
        autoGenerator.global(globalConfig);
        autoGenerator.packageInfo(packageConfig);
        autoGenerator.strategy(strategyConfig);

        autoGenerator.execute(new FreemarkerTemplateEngine());

        if (mode == 2) {
            log.info("新版页面已写入 web/src/views/{}/...", packageConfig.getModuleName());
            log.info("菜单登记 SQL 在 admin-generator/gen-sql/ 下，执行后到角色管理为角色勾选");
        }
    }



}
