package com.lrs.core.system.controller;

import com.lrs.common.constant.Const;
import com.lrs.common.utils.CaptchaUtil;
import com.lrs.common.vo.R;
import com.lrs.core.system.dto.LoginDto;
import com.lrs.core.system.service.ISysUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
            session.setAttribute(Const.SESSION_CODE,img[1]);
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
            session.setAttribute(Const.SESSION_CODE,img[1]);
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
}
