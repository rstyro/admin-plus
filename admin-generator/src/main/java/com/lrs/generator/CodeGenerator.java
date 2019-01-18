package com.lrs.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.lrs.generator.utils.Freemarker;

import java.util.*;

/**
 * Mybatis-plus 的代码生成类
 */
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/admin-core/src/main/java");
        gc.setAuthor("rstyro");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/admin?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Hongkong");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        String modelName=scanner("模块名");
        pc.setModuleName(modelName);
        pc.setParent("com.lrs.core");
        mpg.setPackageInfo(pc);
        String isGeneratorPage = scanner("是否生成页面：1 -- 生成，0 -- 不生成");
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/admin-core/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        if("1".equals(isGeneratorPage)){
            focList.add(new FileOutConfig("templates/pageTemplates/list.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    generatorPage(tableInfo,modelName);
                    // 自定义输入文件名称
                    return projectPath + "/admin-core/src/main/resources/templates/page/"+modelName+"/"+tableInfo.getEntityPath()+"_list.html";
                }
            });

        }

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.lrs.admin");
        strategy.setEntityLombokModel(true);
        strategy.setSuperControllerClass("com.lrs.core.base.BaseController");
        strategy.setInclude(scanner("表名"));
//        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        Map<String,String> map  = new HashMap<>();
        if("1".equals(isGeneratorPage)){
            //如果生成页面的话，自己定义膜拜
            strategy.setRestControllerStyle(false);
            TemplateConfig tc = new TemplateConfig();
            tc.setController("templates/codeTemplates/Controller.java");
            tc.setServiceImpl("templates/codeTemplates/ServiceImpl.java");
            tc.setService("templates/codeTemplates/Service.java");
            tc.setMapper("templates/codeTemplates/mapper.java");
            tc.setEntity("templates/codeTemplates/entity.java");
            tc.setXml("");
            mpg.setTemplate(tc);
        }else {
            strategy.setRestControllerStyle(true);
        }
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.setStrategy(strategy);
        mpg.execute();
    }

    /**
     * 生成页面
     * @param tableInfo
     * @param modelName
     */
    public static void generatorPage(TableInfo tableInfo,String modelName){
        try {
            Map<String,Object> root = new HashMap<String,Object>();		//创建数据模型
            root.put("modelName",modelName);
            setData(root,tableInfo);
            String ftlNmae="list.ftl";
            String projectPath = System.getProperty("user.dir");
            Freemarker.printFile(ftlNmae,root,projectPath + "/admin-core/src/main/resources/templates/page/"+modelName+"/"+tableInfo.getEntityPath()+"_list.html","templates/pageTemplates/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加
     * @param root
     * @param tableInfo
     */
    public static void setData(Map<String,Object> root,TableInfo tableInfo){
        root.put("entityPath", tableInfo.getEntityPath());
        root.put("fields",tableInfo.getFields());
        root.put("controllerName",tableInfo.getControllerName());
        root.put("serviceImplName",tableInfo.getServiceImplName());
        root.put("serviceName",tableInfo.getServiceName());
        root.put("entityName",tableInfo.getEntityName());
        root.put("mapperName",tableInfo.getMapperName());
    }

}
