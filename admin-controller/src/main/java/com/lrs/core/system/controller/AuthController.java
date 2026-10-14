package com.lrs.core.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.lrs.common.constant.Const;
import com.lrs.common.utils.CaptchaUtil;
import com.lrs.common.vo.R;
import com.lrs.core.config.CommonConfig;
import com.lrs.core.system.dto.LoginDto;
import com.lrs.core.system.entity.SysRole;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.service.ISysRoleService;
import com.lrs.core.system.service.ISysUserService;
import com.lrs.core.system.vo.AuthInfoVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;


import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 登录页相关
 */
@Slf4j
@Controller
public class AuthController {


    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private CommonConfig commonConfig;

    /**
     * 验证码
     */
    @SneakyThrows
    @GetMapping("/captcha")
    public void getCode(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        try (OutputStream os = response.getOutputStream()) {
            // 获取图片
            Object[] img = CaptchaUtil.CreateCode(126,40);
            BufferedImage image = (BufferedImage) img[0];
            response.setContentType("image/png");
            ImageIO.write(image, "png", os);
            os.flush();
            session.setAttribute(Const.SessionKey.SESSION_CODE,img[1]);
        } catch (IOException e) {
            log.error("验证码输出异常:{}", e.getMessage(),e);
            // 返回适当的错误响应
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "验证码输出异常");
        }
    }

    /**
     * 验证码
     */
    @SneakyThrows
    @GetMapping("/captcha2")
    public void captcha2(HttpServletRequest request, HttpServletResponse response,int w,int h){
        HttpSession session = request.getSession();
        try (OutputStream os = response.getOutputStream()) {
            // 获取图片
            Object[] img = CaptchaUtil.CreateCode(w,h);
            BufferedImage image = (BufferedImage) img[0];
            response.setContentType("image/png");
            ImageIO.write(image, "png", os);
            os.flush();
            session.setAttribute(Const.SessionKey.SESSION_CODE,img[1]);
        } catch (IOException e) {
            log.error("验证码输出异常:{}", e.getMessage(),e);
            // 返回适当的错误响应
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "验证码输出异常");
        }
    }


    /**
     * 用户登录
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(HttpServletRequest request,@RequestBody LoginDto dto) throws Exception {
        return R.ok(sysUserService.login(request,dto));
    }

    /**
     * 用户注销
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public R logout(HttpServletRequest request){
        return R.ok(sysUserService.logout(request));
    }

    /**
     * 当前登录用户的认证信息
     * @description 供前后端分离的 web 前端做按钮级权限控制（v-permission）与系统名展示，
     *   数据口径与 Sa-Token 服务端校验一致（同一套 sys_menu.permit 权限 KEY）
     * @return R<AuthInfoVo> 用户、角色名、权限 KEY 列表、系统名
     */
    @GetMapping("/auth/info")
    @ResponseBody
    public R<AuthInfoVo> authInfo() {
        Long userId = StpUtil.getLoginId(0L);
        SysUser sysUser = JSON.parseObject(
                JSON.toJSONString(StpUtil.getSession().get(Const.SessionKey.SESSION_USER)), SysUser.class);
        if (sysUser != null) {
            sysUser.setPassword(null);
            sysUser.setSalt(null);
        }
        List<String> roles = sysRoleService.getUserRoleList(userId).stream()
                .map(SysRole::getRoleName)
                .collect(Collectors.toList());
        // 目录/菜单节点的 permit 为空，过滤后前端权限判断才不会被空值干扰
        List<String> permissions = sysUserService.getUserPermissionList(userId).stream()
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        AuthInfoVo vo = new AuthInfoVo()
                .setUser(sysUser)
                .setRoles(roles)
                .setPermissions(permissions)
                .setSystemName(commonConfig.getSystemName());
        return R.ok(vo);
    }
}
