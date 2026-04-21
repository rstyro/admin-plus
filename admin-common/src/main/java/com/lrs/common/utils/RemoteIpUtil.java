package com.lrs.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 获取远程IP工具类
 */
@Slf4j
public class RemoteIpUtil {

    // IP头字段按优先级排序
    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP",
            "X-Cluster-Client-IP"
    };

    // 需要标准化的本地地址
    private static final Set<String> LOCAL_IPS = Set.of("0:0:0:0:0:0:0:1", "::1", "localhost", "127.0.0.1");

    /**
     * 获取客户端真实IP地址
     * @param request HttpServletRequest对象
     * @return 客户端真实IP地址，获取失败返回空字符串
     */
    public static String getRemoteIp(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = null;
        // 按优先级检查各个头部字段
        for (String header : IP_HEADERS) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }
        // 如果头部都没有有效IP，使用远程地址
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况（如：X-Forwarded-For: client, proxy1, proxy2）
        ip = extractFirstValidIp(ip);
        // 标准化本地地址
        return normalizeIp(ip);
    }

    /**
     * 验证IP是否有效
     */
    private static boolean isValidIp(String ip) {
        return StringUtils.hasText(ip) &&
                !"unknown".equalsIgnoreCase(ip) &&
                !isLocalIp(ip);
    }

    /**
     * 判断是否是本地IP
     */
    private static boolean isLocalIp(String ip) {
        if (!StringUtils.hasText(ip)) {
            return false;
        }

        // 检查已知的本地地址
        if (LOCAL_IPS.contains(ip.toLowerCase())) {
            return true;
        }

        // 检查127.x.x.x网段
        return ip.startsWith("127.");
    }

    /**
     * 从可能包含多个IP的字符串中提取第一个有效IP
     */
    private static String extractFirstValidIp(String ipChain) {
        if (!StringUtils.hasText(ipChain)) {
            return "";
        }
        // 如果不存在逗号分隔，直接返回
        if (!ipChain.contains(",")) {
            return ipChain.trim();
        }
        // 处理多个IP的情况
        String[] ips = ipChain.split(",\\s*");
        for (String ip : ips) {
            String trimmedIp = ip.trim();
            if (isValidIp(trimmedIp)) {
                return trimmedIp;
            }
        }

        // 如果没有找到完全有效的IP，返回第一个
        return ips[0].trim();
    }

    /**
     * 标准化IP地址
     */
    private static String normalizeIp(String ip) {
        if (!StringUtils.hasText(ip)) {
            return "";
        }

        // 标准化本地地址
        switch (ip.toLowerCase()) {
            case "0:0:0:0:0:0:0:1":
            case "::1":
            case "localhost":
                return "127.0.0.1";
            default:
                if (ip.startsWith("127.")) {
                    return "127.0.0.1";
                }
                return ip;
        }
    }

    /**
     * 获取详细的IP调试信息（用于日志记录和问题排查）
     */
    public static Map<String, String> getIpDebugInfo(HttpServletRequest request) {
        Map<String, String> debugInfo = new LinkedHashMap<>();
        if (request == null) {
            return debugInfo;
        }
        debugInfo.put("Final-IP", getRemoteIp(request));
        debugInfo.put("Remote-Addr", request.getRemoteAddr());
        // 收集所有相关的IP头信息
        for (String header : IP_HEADERS) {
            String value = request.getHeader(header);
            if (StringUtils.hasText(value)) {
                debugInfo.put(header, value);
            }
        }
        return debugInfo;
    }

    /**
     * 批量获取IP（适用于批量处理场景）
     */
    public static List<String> getRemoteIps(List<HttpServletRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> results = new ArrayList<>(requests.size());
        for (HttpServletRequest request : requests) {
            results.add(getRemoteIp(request));
        }
        return results;
    }

    /**
     * 安全获取IP，避免异常影响主流程
     */
    public static String getRemoteIpSafely(HttpServletRequest request) {
        try {
            return getRemoteIp(request);
        } catch (Exception e) {
            // 记录错误但不要影响主流程
            log.error("获取远程IP失败，err:{}",e.getMessage(), e);
            return "";
        }
    }
}