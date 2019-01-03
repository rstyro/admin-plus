package com.lrs.common.annotation;

import java.lang.annotation.*;

/**
 * 权限注解
 * url 哪个请求列表的权限
 * type 是这个列表的那个权限（增删改查）
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String url() default "list";
    PermissionType type() default PermissionType.QUERY;
}
