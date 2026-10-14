package com.lrs.generator.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.*;

public class BaseGenerator {

    /**
     * 作者
     */
    public static final String AUTHOR = "rstyro";

    /**
     * 父级包名
     */
    public static final String BASE_PACKAGES = "com.lrs.generator";
    /**
     * 数据库链接相关信息
     */
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/admin?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=GMT%2B8";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "root";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 数据源配置
     */
    public static DataSourceConfig.Builder getDataSourceConfig() {
        return new DataSourceConfig.Builder(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static GlobalConfig.Builder getGlobalConfig(String outPath) {
        return new GlobalConfig.Builder()
                .author(AUTHOR) // 设置作者
//               .enableSwagger() // 开启 swagger 模式
//               .enableSpringdoc()
                .disableOpenDir()
                .outputDir(outPath + "/src/main/java");
    }

    public static PackageConfig.Builder getPackageConfig(String outPath,String moduleName) {
        return new PackageConfig.Builder().parent(BASE_PACKAGES)
                // 设置父包模块名
                .moduleName(moduleName)
                // 设置mapperXml生成路径
                .pathInfo(Collections.singletonMap(OutputFile.xml, outPath + "/src/main/resources/mapper/"+moduleName));
    }

    /**
     * 自定义策略，生成自定义模版页面
     */
    public static StrategyConfig.Builder getStrategyConfig(List<String> includes) {        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.addInclude(includes) // 设置需要生成的表名
                // 设置过滤表前缀
                .addTablePrefix("t_", "c_")
                .entityBuilder()
                .enableLombok()
                .enableTableFieldAnnotation()
                .enableChainModel()
                .naming(NamingStrategy.underline_to_camel)//数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
                .columnNaming(NamingStrategy.underline_to_camel)//表字段映射实体属性命名规则：默认null，不指定按照naming执行
                .idType(IdType.AUTO)//添加全局主键类型
                .mapperBuilder()
                .mapperAnnotation(Mapper.class)//开启mapper注解
//                            .enableBaseResultMap()//启用xml文件中的BaseResultMap 生成
//                .enableBaseColumnList()//启用xml文件中的BaseColumnList
                .controllerBuilder()
                .enableRestStyle();

        builder.entityBuilder()
                .javaTemplate("/templates/codeTemplates/entity.java") // 设置实体类模板
//                            .disable() // 禁用实体类生成
                .serviceBuilder()
//                            .disableService() // 禁用 Service 层生成
                .serviceTemplate("/templates/codeTemplates/service.java") // 设置 Service 模板
                .serviceImplTemplate("/templates/codeTemplates/serviceImpl.java") // 设置 ServiceImpl 模板
                .controllerBuilder()
                .template("/templates/codeTemplates/controller.java")
                .build();

        return builder;
    }

    public static StrategyConfig.Builder getDefaultStrategyConfig(List<String> includes) {
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.addInclude(includes) // 设置需要生成的表名
                // 设置过滤表前缀
                .addTablePrefix("t_", "c_")
                .entityBuilder()
                .enableLombok()
                .enableTableFieldAnnotation()
                .enableChainModel()
                .naming(NamingStrategy.underline_to_camel)//数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
                .columnNaming(NamingStrategy.underline_to_camel)//表字段映射实体属性命名规则：默认null，不指定按照naming执行
                .idType(IdType.AUTO)//添加全局主键类型
                .mapperBuilder()
                .mapperAnnotation(Mapper.class)//开启mapper注解
//                            .enableBaseResultMap()//启用xml文件中的BaseResultMap 生成
//                .enableBaseColumnList()//启用xml文件中的BaseColumnList
                .controllerBuilder()
                .enableRestStyle();

        builder.entityBuilder()
//                .javaTemplate("/templates/codeTemplates/entity.java") // 设置实体类模板
                .build();
        return builder;
    }

    /**
     * 注入配置：按模式追加自定义产物
     * @param param      模板自定义参数
     * @param mode       0=仅后端代码；1=追加旧版页面（admin-generator 下 html）；2=追加新版 web 页面与菜单 SQL
     * @param projectRoot 仓库根目录（绝对路径）
     * @param moduleName 模块名
     */
    public static InjectionConfig.Builder getInjectionConfig(Map<String, Object> param, int mode, String projectRoot, String moduleName) {
        // 自定义代码参数
        InjectionConfig.Builder builder = new InjectionConfig.Builder()
                .beforeOutputFile((tableInfo, objectMap) -> {
                    System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                })
                .customMap(param);
        if (mode == 1) {
            // 旧版 Vue2 + Thymeleaf 页面（对照用），写到 admin-generator 输出目录
            builder.customFile(new CustomFile.Builder()
                    .formatNameFunction(TableInfo::getName)
                    .fileName(".html")
                    .enableFileOverride()
                    .templatePath("/templates/pageTemplates/list.ftl").build());
        } else if (mode == 2) {
            // 新版 Vue3 页面：直接写到 web/src/views/{模块}/{实体}/index.vue
            // filePath 为绝对目录，文件名 = {entityPath}/index.vue（entityPath 无分隔符，天然形成子目录）
            builder.customFile(new CustomFile.Builder()
                    .filePath(projectRoot + "/web/src/views/" + moduleName)
                    .formatNameFunction(TableInfo::getEntityPath)
                    .fileName("/index.vue")
                    .enableFileOverride()
                    .templatePath("/templates/vueTemplates/index.vue.ftl").build());
            // 菜单登记 SQL：写到 admin-generator/gen-sql/
            builder.customFile(new CustomFile.Builder()
                    .filePath(projectRoot + "/admin-generator/gen-sql")
                    .formatNameFunction(tableInfo -> moduleName + "_" + tableInfo.getEntityPath())
                    .fileName("_menu.sql")
                    .enableFileOverride()
                    .templatePath("/templates/vueTemplates/menu.sql.ftl").build());
        }
        return builder;
    }


    public static String scannerNext(String message) {
        System.out.println(message);
        String nextLine = scanner.nextLine();
        if (!StringUtils.hasText(nextLine)) {
            // 如果输入空行继续等待
            return scanner.next();
        }
        return nextLine;
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

}
