package com.lrs.core.system.event;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lrs.common.utils.RegionUtils;
import com.lrs.core.base.BaseController;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志记录 Event
 */
@Slf4j
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class LoginInfoEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    private String status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private LocalDateTime createTime;

    /**
     * 设置 登录信息
     */
    public void setInfo(HttpServletRequest request){
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String ip = BaseController.getRemoteIP(request);
        String address = RegionUtils.getCityInfo(ip);
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        this.ipaddr=ip;
        this.location=address;
        this.os=os;
        this.browser=browser;

        StringBuilder logInfoBuilder = new StringBuilder();
        logInfoBuilder.append(getBlock(ip));
        logInfoBuilder.append(address);
        logInfoBuilder.append(getBlock(getLoginName()));
        logInfoBuilder.append(getBlock(getStatus()));
        logInfoBuilder.append(getBlock(getMsg()));
        // 打印信息到日志
        log.info("登录日志={}",logInfoBuilder.toString());
    }

    private String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
