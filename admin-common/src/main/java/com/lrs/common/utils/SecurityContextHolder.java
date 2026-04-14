package com.lrs.common.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.lrs.common.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 上下文工具类
 * @author rstyro
 */
public class SecurityContextHolder {

    // ThreadLocal变量定义
    private static final TransmittableThreadLocal<String> TRACKER_ID_HOLDER = new TransmittableThreadLocal<>();
    // 分页
    private static final TransmittableThreadLocal<Integer> PAGE_NO_HOLDER = new TransmittableThreadLocal<>();
    private static final TransmittableThreadLocal<Integer> PAGE_SIZE_HOLDER = new TransmittableThreadLocal<>();
    // 用户token相关
    private static final TransmittableThreadLocal<String> TOKEN_HOLDER = new TransmittableThreadLocal<>();
    private static final TransmittableThreadLocal<Long> USER_ID_HOLDER = new TransmittableThreadLocal<>();

    // 默认值常量
    private static final int DEFAULT_PAGE_NO = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 1000;

    public static String getTrackerId() {
        String trackerId = TRACKER_ID_HOLDER.get();
        if (trackerId == null) {
            trackerId = IdUtil.fastSimpleUUID();
            setTrackerId(trackerId);
        }
        return trackerId;
    }

    public static void setTrackerId(String trackerId) {
        TRACKER_ID_HOLDER.set(trackerId);
    }

    //=== 分页相关方法 ===
    public static int getPageNo() {
        Integer pageNo = PAGE_NO_HOLDER.get();
        return pageNo != null && pageNo > 0 ? pageNo : DEFAULT_PAGE_NO;
    }

    public static void setPageNo(Integer pageNo) {
        if (pageNo != null && pageNo > 0) {
            PAGE_NO_HOLDER.set(pageNo);
        } else {
            PAGE_NO_HOLDER.set(DEFAULT_PAGE_NO);
        }
    }

    public static int getPageSize() {
        Integer pageSize = PAGE_SIZE_HOLDER.get();
        return pageSize != null && pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    public static void setPageSize(Integer pageSize) {
        if (pageSize != null && pageSize > 0) {
            // 限制最大分页大小，防止内存溢出
            int safePageSize = Math.min(pageSize, MAX_PAGE_SIZE);
            PAGE_SIZE_HOLDER.set(safePageSize);
        } else {
            PAGE_SIZE_HOLDER.set(DEFAULT_PAGE_SIZE);
        }
    }

    public static String getToken() {
        return TOKEN_HOLDER.get();
    }

    public static void setToken(String token) {
        TOKEN_HOLDER.set(token);
    }

    public static boolean hasToken() {
        return StringUtils.hasLength(getToken());
    }

    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static boolean hasUserId() {
        return getUserId() != null;
    }


    /**
     * 批量设置上下文参数
     */
    public static void setContext(String trackerId, Integer pageNo, Integer pageSize,
                                  String token, Long userId) {
        if (StringUtils.hasLength(trackerId)) {
            setTrackerId(trackerId);
        }
        if (pageNo != null) {
            setPageNo(pageNo);
        }
        if (pageSize != null) {
            setPageSize(pageSize);
        }
        if (StringUtils.hasLength(token)) {
            setToken(token);
        }
        if (userId != null) {
            setUserId(userId);
        }
    }

    /**
     * 从HttpServletRequest初始化上下文
     */
    public static void initFromRequest(HttpServletRequest request) {
        String trackerId = getHeaderOrParam(request, Const.HeaderKey.TRACKER_ID);
        String pageNoStr = getHeaderOrParam(request, Const.HeaderKey.PAGE_NO);
        String pageSizeStr = getHeaderOrParam(request, Const.HeaderKey.PAGE_SIZE);
        String token = getHeaderOrParam(request, Const.HeaderKey.TOKEN);
        String userIdStr = getHeaderOrParam(request, Const.HeaderKey.USER_ID);

        setContext(
                StringUtils.hasLength(trackerId) ? trackerId : IdUtil.fastSimpleUUID(),
                parsePositiveInt(pageNoStr, DEFAULT_PAGE_NO),
                parsePositiveInt(pageSizeStr, DEFAULT_PAGE_SIZE),
                token,
                parseLong(userIdStr)
        );
    }

    /**
     * 获取当前上下文快照
     */
    public static Map<String, Object> getContextSnapshot() {
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("trackerId", getTrackerId());
        snapshot.put("pageNo", getPageNo());
        snapshot.put("pageSize", getPageSize());
        snapshot.put("token", hasToken()); // 脱敏处理
        snapshot.put("userId", getUserId());
        return snapshot;
    }

    /**
     * 重置为默认值
     */
    public static void reset() {
        setTrackerId(IdUtil.fastSimpleUUID());
        setPageNo(DEFAULT_PAGE_NO);
        setPageSize(DEFAULT_PAGE_SIZE);
        setToken(null);
        setUserId(null);
    }

    /**
     * 清理所有ThreadLocal变量，防止内存泄漏
     */
    public static void clear() {
        TRACKER_ID_HOLDER.remove();
        PAGE_NO_HOLDER.remove();
        PAGE_SIZE_HOLDER.remove();
        TOKEN_HOLDER.remove();
        USER_ID_HOLDER.remove();
    }


    //=== 私有工具方法 ===
    private static String getHeaderOrParam(HttpServletRequest request, String key) {
        String headerValue = request.getHeader(key);
        if (StringUtils.hasLength(headerValue)) {
            return headerValue;
        }
        return request.getParameter(key);
    }

    private static Integer parsePositiveInt(String str, int defaultValue) {
        if (!StringUtils.hasLength(str) || !StrUtil.isNumeric(str)) {
            return defaultValue;
        }
        try {
            int value = Integer.parseInt(str);
            return value > 0 ? value : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static Long parseLong(String str) {
        if (!StringUtils.hasLength(str) || !StrUtil.isNumeric(str)) {
            return null;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}