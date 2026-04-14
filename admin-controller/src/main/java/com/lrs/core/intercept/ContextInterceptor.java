package com.lrs.core.intercept;

import com.lrs.common.utils.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 上下文参数注入
 */
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 初始化所有上下文参数
            SecurityContextHolder.initFromRequest(request);
            return true;
        } catch (Exception e) {
            // 初始化失败时使用默认值，确保请求继续处理
            SecurityContextHolder.reset();
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清理ThreadLocal，防止内存泄漏
        SecurityContextHolder.clear();
    }
}
