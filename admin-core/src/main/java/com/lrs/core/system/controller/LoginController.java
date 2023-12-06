package com.lrs.core.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.Const;
import com.lrs.common.vo.R;
import com.lrs.common.exception.ApiException;
import com.lrs.common.utils.CaptchaUtil;
import com.lrs.common.vo.TabsVo;
import com.lrs.core.system.config.CommonConfig;
import com.lrs.core.system.dto.LoginDto;
import com.lrs.core.system.entity.SysUser;
import com.lrs.core.system.service.ISysUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

/**
 * 自定义错误页面
 */
@Slf4j
@Controller
public class LoginController {

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private ISysUserService sysUserService;

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
        SysUser sysUser = Convert.convert(SysUser.class,StpUtil.getSession().get(Const.SESSION_USER));
        model.addAttribute("systemName",commonConfig.getSystemName());
        model.addAttribute("user",sysUser);
        model.addAttribute("adminName",sysUser.getNickName());
        model.addAttribute("avatar",sysUser.getPicUrl());
        model.addAttribute("userId",sysUser.getId());
        // tabs菜单
        List<TabsVo> tabMenuList = sysUserService.getTabMenuList(StpUtil.getLoginId(0l));
        model.addAttribute("tabs",tabMenuList);
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
     * icon
     */
    @GetMapping("/icon")
    public String icon(){
        return "icon";
    }

    /**
     * 验证码
     */
    @SneakyThrows
    @GetMapping("/captcha")
    public void getCode(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        try (OutputStream os = response.getOutputStream()) {
            // 获取图片
            Object[] img = CaptchaUtil.CreateCode();
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
    public R logout(){
        StpUtil.logout();
        return R.ok();
    }
}
