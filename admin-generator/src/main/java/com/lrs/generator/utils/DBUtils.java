package com.lrs.generator.utils;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

public class DBUtils {
    private static String url="jdbc:mysql://localhost:3306/admin?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static String driverName="com.mysql.cj.jdbc.Driver";
    private static String username="root";
    private static String password="root";

    /**
     * 获取数据源配置
     * @return
     */
    public static DataSourceConfig getDateSource(){
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        return dsc;
    }
}
