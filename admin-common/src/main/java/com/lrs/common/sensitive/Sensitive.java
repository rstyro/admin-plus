package com.lrs.common.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据脱敏注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveHandler.class)
public @interface Sensitive {
    /**
     * 脱敏策略
     */
    SensitiveStrategy strategy();

    /**
     * 角色权限KEY,有权限则不脱敏
     */
    String roleKey() default "";

    /**
     * 权限key，有权限则不脱敏
     */
    String perms() default "";
}
