[![build](https://img.shields.io/badge/build-passing-success.svg)]()
[![jdkversions](https://img.shields.io/badge/Java-1.8%2B-success.svg)]()
[![maven](https://img.shields.io/badge/Maven-V3.3.9-success.svg)]()
[![SpringBoot](https://img.shields.io/badge/SpringBoot-V2.1.0-success.svg)]()
[![Mybatis_Plus](https://img.shields.io/badge/Mybatis_Plus-V3.0.1-success.svg)]()
[![Apache2.0](https://img.shields.io/badge/Apache-2.0-success.svg)]()

# admin-plus
这个项目是我把以前的 [admin](https://github.com/rstyro/admin) 项目重新整理了一个

## Maven 模块包
+ admin-common     
公用包，这个是存放一些常用的工具类，把它当成一个jar 使用.  
所以可以 用maven 命令，把它安装到本地仓库
+ admin-core       
核心包，这个就是admin 的 所有核心内容了，部署也是部署这个包就可以了，  
都是springboot 项目，部署应该也不难  
如果不怎么清楚部署的话，可以看我以前的文章：[Spring Boot-打包部署](https://rstyro.github.io/blog/2017/09/20/Spring%20Boot%20(%E5%8D%81)%EF%BC%9A%E6%89%93%E5%8C%85%E9%83%A8%E7%BD%B2/)

+ admin-generator  
代码生成器,因为是后台吗，所以页面是少不了的，然后有些页面几乎长的都差不多一样，所以抽出来一个模板，
这样可以节省很多时间，也不需要做重复的操作,如果模板不适合你们的业务，可以自己修改模板
`codeTemplates`、`pageTemplates` 这两个下面的`ftl` 文件


## 功能
+ 菜单权限
+ 角色权限
+ 用户管理

## 快速使用
+ 导入`mysql` 脚本 `admin.sql` 到你的数据库
+ 修改`resources/application.yml` 下的数据库用户名与密码  
脚本默认的账户：admin/amdin
+ 启动 `Application` 即可

## Tip
#### 1、demo 示例
`admin-core` 模块下的 `com.lrs.core.act` 就是一个 demo 包  
对应的表就是 `act_acticle`,这个包里面的代码大部分是代码生成器生成的  
可以仿造它写，就可以使用了，会使用之后，把与其想关的删除掉即可
#### 2、MybatisPlus 文档：  
[https://mp.baomidou.com/guide/config.html](https://mp.baomidou.com/guide/config.html)


#### 3、代码生成器教程示例图

![示例图](https://github.com/rstyro/admin-plus/blob/master/1.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/2.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/3.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/4.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/5.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/6.png)
