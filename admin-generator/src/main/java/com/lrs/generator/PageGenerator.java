package com.lrs.generator;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.lrs.generator.utils.DBUtils;
import com.lrs.generator.utils.Freemarker;

import java.util.*;

public class PageGenerator {

    public static void main(String[] args) throws Exception {

        Map<String,Object> root = new HashMap<String,Object>();		//创建数据模型
        List<String[]> fieldList = new ArrayList<String[]>();   	//属性集合			========参数4
        int zindex=10;
        for(int i=0; i< zindex; i++){
            String[] fileds = {"test"+i,"test"+i+1,"test"+i+2,"test"+i+3,"test"+i+4,"test"+i+5,"test"+i+6};
            fieldList.add(fileds);	//属性放到集合里面
        }
        root.put("fieldList", fieldList);
        root.put("TITLE", "test");									//说明
        root.put("packageName", "com.lrs");						//包名
        root.put("objectName", "TbTest");							//类名
        root.put("objectNameLower", "tbTest");		             //类名(全小写)
        root.put("objectNameUpper", "TBTEST");		            //类名(全大写)
        root.put("tabletop", "");								//表前缀
        root.put("nowDate", new Date());
        String ftlNmae="list_Template.ftl";
        String projectPath = System.getProperty("user.dir");
        Freemarker.printFile(ftlNmae,root,projectPath + "/admin-core/src/main/resources/list.html","templates/pageTemplates/");

    }
}
