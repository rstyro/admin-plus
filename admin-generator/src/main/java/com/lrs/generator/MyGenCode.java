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
        AutoGenerator autoGenerator =  new AutoGenerator(getDataSourceConfig().build());
        //配置相关
        GlobalConfig globalConfig = getGlobalConfig(outPath).build();
        PackageConfig packageConfig = getPackageConfig(outPath,scannerNext("输入模块名（如果需要生成页面模块要确定）：")).build();
        param.put("basePackage", packageConfig.getParent());
        String result = scannerNext("是否需要生成页面，1=生成，0=取消");
        List<String> tables = getTables(scannerNext("请输入表名，多个英文逗号分隔？所有输入 all"));
        StrategyConfig strategyConfig=null;
        if("1".equals(result)){
            strategyConfig = getStrategyConfig(tables).build();

            InjectionConfig injectionConfig = getInjectionConfig(param).build();
            // 注入配置，可生成自定义 页面、VO 、DTO 等
            autoGenerator.injection(injectionConfig);
        }else {
            strategyConfig = getDefaultStrategyConfig(tables).build();
        }
        autoGenerator.global(globalConfig);
        autoGenerator.packageInfo(packageConfig);
        autoGenerator.strategy(strategyConfig);

        autoGenerator.execute(new FreemarkerTemplateEngine());

    }



}
