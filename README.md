# admin-plus

## admin-common     公用包
## admin-core       核心包
## admin-generator  代码生成器
### 主要技术栈：Springboot 2 + MybatisPlus 

#### 已有菜单权限、角色权限、用户管理
#### MybatisPlus 文档：[https://mp.baomidou.com/guide/config.html](https://mp.baomidou.com/guide/config.html)
#### Redis 配置已设置好，不用把配置注释即可
#### 内含代码生成器，一键运行，自动生成页面超级方便，下面是教程示例图
#### 不太清楚怎么用的提 Issues
#### 导入mysql 脚本。admin.sql
#### 账号密码：admin/admin

### 温馨提示：如果表结构有datetime 类型的，在实体类要加个`DateTimeFormat` 、`JsonFormat`注解，如下
```
/**
 * Y/N
 * 逻辑删除的注解
 */
@TableLogic
private String isDeleted;

@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createTime;

@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime modifyTime;

```

![示例图](https://github.com/rstyro/admin-plus/blob/master/1.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/2.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/3.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/4.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/5.png)
![示例图](https://github.com/rstyro/admin-plus/blob/master/6.png)
