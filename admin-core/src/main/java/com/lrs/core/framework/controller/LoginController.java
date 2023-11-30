package com.lrs.core.framework.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.Const;
import com.lrs.common.constant.R;
import com.lrs.common.exception.ApiException;
import com.lrs.common.utils.CaptchaUtil;
import com.lrs.core.framework.config.CommonConfig;
import com.lrs.core.framework.dto.LoginDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 自定义错误页面
 */
@Slf4j
@Controller
public class LoginController {

    @Resource
    private CommonConfig commonConfig;

    /**
     * 登录页
     */
    @GetMapping(value = {"/","/toLogin"})
    public String index(){
        return "login";
    }

    /**
     * 首页
     * @param model model
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("adminName",commonConfig.getName());
        model.addAttribute("systemName",commonConfig.getSystemName());
        return "index";
    }

    /**
     * 欢迎页
     */
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 验证码
     */
    @SneakyThrows
    @GetMapping(value = "/captcha")
    public void getCode(HttpServletRequest request, HttpServletResponse response){
        try (OutputStream os = response.getOutputStream()) {
            // 获取图片
            Object[] img = CaptchaUtil.CreateCode();
            BufferedImage image = (BufferedImage) img[0];
            response.setContentType("image/png");
            ImageIO.write(image, "png", os);
            os.flush();
            // 用于验证的字符串存入session
            HttpSession session = request.getSession();
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
        String codeStr = (String) request.getSession().getAttribute(Const.SESSION_CODE);
        if(!dto.getCode().equals(codeStr)){
            throw new ApiException(ApiResultEnum.SYSTEM_CODE_ERROR);
        }
        StpUtil.login(1);
        return R.ok();
    }

    /**
     * 用户注销
     * @return
     */
    @PostMapping("/logout")
    public R logout() throws Exception {
        System.out.println("1111");
        StpUtil.logout();
        return R.ok();
    }
}
