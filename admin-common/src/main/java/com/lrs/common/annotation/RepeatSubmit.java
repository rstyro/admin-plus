package com.lrs.common.annotation;

import com.lrs.common.enums.LockType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义注解防止表单重复提交
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 锁定的key，支持SpEL表达式
     * 示例: #user.id, #requestParam
     */
    String key() default "";

    /**
     * 锁类型
     */
    LockType lockType() default LockType.PARAM;

    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    int lockTime() default 5000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 提示消息 支持国际化 格式为 {code}
     */
    String message() default "操作过于频繁，请稍候再试";

}
