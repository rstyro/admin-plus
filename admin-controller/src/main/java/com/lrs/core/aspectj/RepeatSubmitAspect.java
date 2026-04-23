package com.lrs.core.aspectj;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.lrs.common.annotation.RepeatSubmit;
import com.lrs.common.constant.Const;
import com.lrs.common.enums.RedisKeyEnum;
import com.lrs.common.exception.ServiceException;
import com.lrs.common.utils.AopUtil;
import com.lrs.common.utils.RedisSimulation;
import com.lrs.common.utils.RemoteIpUtil;
import com.lrs.core.base.BaseController;
import com.lrs.core.system.entity.SysUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 防止重复提交
 */
@Slf4j
@Aspect
@Component
public class RepeatSubmitAspect {

    private static final String LOCK_KEY_PREFIX = RedisKeyEnum.REPEAT_SUBMIT.getKey();
    private static final String SPEL_PARSER = "#";
    /**
     * redis 模拟，有条件可集成Redis
     */
    private static final RedisSimulation redisSimulation = SpringUtil.getBean(RedisSimulation.class);

    /**
     * 环绕通知
     */
    @Around(value = "@annotation(repeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {
        // 生成锁的key
        String lockKey = generateLockKey(joinPoint, repeatSubmit);
        String requestId = IdUtil.simpleUUID();
        // 尝试获取锁
        Boolean success = redisSimulation.tryLock(lockKey, requestId, repeatSubmit.lockTime(), repeatSubmit.timeUnit());
        if(Boolean.FALSE.equals(success)) {
            // 获取锁失败，抛出重复提交异常
            throw new ServiceException(repeatSubmit.message());
        }
        try {
            log.debug("获取请求锁key：{} 成功",lockKey);
            // 获取锁成功，执行目标方法
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error("around,err:{}",e.getMessage(), e);
            // 方法执行失败时，才会删除锁
            redisSimulation.releaseLock(lockKey, requestId);
            throw e;
        }
    }

    /**
     * 生成锁的key - 核心逻辑
     */
    private String generateLockKey(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) {
        StringBuilder keyBuilder = new StringBuilder(LOCK_KEY_PREFIX);
        // 基础key（方法签名或自定义key）
        String baseKey = buildBaseKey(joinPoint, repeatSubmit);
        keyBuilder.append(baseKey);

        // 处理key生成策略
        String keyByStrategy = buildKeyByStrategy(joinPoint, repeatSubmit);
        if (StringUtils.hasText(keyByStrategy)) {
            keyBuilder.append(":").append(keyByStrategy);
        }
        return keyBuilder.toString();
    }

    /**
     * 构建基础key
     */
    private String buildBaseKey(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) {
        String key = repeatSubmit.key();
        if (StringUtils.hasText(key)) {
            // 处理SpEL表达式
            if (key.contains(SPEL_PARSER)) {
                return AopUtil.parseSpel(key, joinPoint);
            }
            return key;
        }
        // 默认使用类名+方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getDeclaringClass().getSimpleName() + ":" + method.getName();
    }

    /**
     * 根据策略构建key
     */
    private String buildKeyByStrategy(ProceedingJoinPoint joinPoint, RepeatSubmit antiResubmit) {
        StringBuilder keyBuilder = new StringBuilder();

        HttpServletRequest request = BaseController.getRequest();
        // 根据锁类型添加不同标识
        switch (antiResubmit.lockType()) {
            case SYSTEM_USER:
                SysUser loginSysUser = BaseController.getLoginSysUser();
                String userId = loginSysUser != null?String.valueOf(loginSysUser.getId()):"";
                if (StringUtils.hasText(userId)) {
                    addSeparator(keyBuilder);
                    keyBuilder.append("system_user:").append(userId);
                }
                break;
            case IP:
                String clientIp = RemoteIpUtil.getRemoteIpSafely(request);
                if (StringUtils.hasText(clientIp)) {
                    addSeparator(keyBuilder);
                    keyBuilder.append("ip:").append(clientIp);
                }
                break;
            case SESSION:
                String sessionId = StpUtil.getSession().getId();
                if (StringUtils.hasText(sessionId)) {
                    addSeparator(keyBuilder);
                    keyBuilder.append("session:").append(sessionId);
                }
                break;
            case PARAM:
                // 参数级别的锁，使用所有参数hash
                String paramHash = generateParamHash(joinPoint);
                if (StringUtils.hasText(paramHash)) {
                    addSeparator(keyBuilder);
                    keyBuilder.append("param:").append(paramHash);
                }
                break;
        }

        return keyBuilder.toString();
    }

    private String generateParamHash(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return "no_params";
            }
            String paramStr = Arrays.stream(args)
                    .filter(o-> Objects.nonNull(o) && !(o instanceof MultipartFile))
                    .map(Object::toString)
                    .collect(Collectors.joining("|"));
            return DigestUtils.md5DigestAsHex(paramStr.getBytes());
        } catch (Exception e) {
            log.warn("生成参数hash失败", e);
            return "error";
        }
    }

    private void addSeparator(StringBuilder builder) {
        if (!builder.isEmpty()) {
            builder.append(":");
        }
    }

}
