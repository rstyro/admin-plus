package com.lrs.core.admin.controller;


import com.lrs.common.constant.Const;
import com.lrs.common.constant.Result;
import com.lrs.core.admin.dto.LoginDTO;
import com.lrs.core.admin.entity.Menu;
import com.lrs.core.admin.entity.User;
import com.lrs.core.admin.service.ILoginService;
import com.lrs.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Controller
public class LoginController extends BaseController {
    @Value("${admin.name}")
    private String adminName;

    @Autowired
    private ILoginService loginService;

    /**
     * 入口
     * @return
     */
    @RequestMapping(value={"/","/toLogin"},method= RequestMethod.GET)
    public String toLogin(){
        return "login";
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/index")
    public String index(Model model){
        try {
            List<Menu> allMenu = (List<Menu>) this.getSession().getAttribute(Const.SESSION_ALL_MENU);
            if(allMenu != null){
                model.addAttribute("menus", allMenu);
            }
            model.addAttribute("adminName", adminName);
            model.addAttribute("userName", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getNickName());
            model.addAttribute("userPath", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getPicPath());
            model.addAttribute("userStatus", "在线");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }


    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value={"/login"},method=RequestMethod.POST)
    @ResponseBody
    public Result login(LoginDTO dto) throws Exception {
        return loginService.login(dto, this.getSession());
    }

    /**
     * 用户注销
     * @return
     */
    @RequestMapping("/logout")
    public String logout() throws Exception {
        return loginService.logout(this.getSession());
    }

}
