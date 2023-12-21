package com.lrs.core.intercept;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lrs.common.constant.Const;
import com.lrs.common.vo.ContextUtil;
import com.lrs.common.vo.ContextVo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 上下文参数注入
 */
public class ContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String pageNo = Optional.ofNullable(request.getHeader(Const.HeaderKey.PAGE_NO)).orElse(request.getParameter(Const.HeaderKey.PAGE_NO));
        String pageSize = Optional.ofNullable(request.getHeader(Const.HeaderKey.PAGE_SIZE)).orElse(request.getParameter(Const.HeaderKey.PAGE_SIZE));
        String trackerId = Optional.ofNullable(request.getHeader(Const.HeaderKey.TRACKER_ID)).orElse(request.getParameter(Const.HeaderKey.TRACKER_ID));
        ContextVo contextVo = new ContextVo();
        contextVo.setTrackerId(StringUtils.hasLength(trackerId)?trackerId:IdUtil.fastSimpleUUID());
        contextVo.setPageNo(StringUtils.hasLength(pageNo)&&StrUtil.isNumeric(pageNo)?Integer.parseInt(pageNo):1);
        contextVo.setPageSize(StringUtils.hasLength(pageSize)&&StrUtil.isNumeric(pageSize)?Integer.parseInt(pageSize):10);
        ContextUtil.setVoLocal(contextVo);
        return true;
    }

}
