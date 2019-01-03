package com.lrs.core.sys.controller;


import com.lrs.common.annotation.Permission;
import com.lrs.common.annotation.PermissionType;
import com.lrs.core.base.BaseController;
import com.lrs.core.entity.Const;
import com.lrs.core.sys.dto.UserDTO;
import com.lrs.core.sys.entity.User;
import com.lrs.core.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {
    private final static String qxurl="sys/role/list";

    @Autowired
    private IUserService userService;

    /**
     * 用户列表
     * @return
     */
    @GetMapping(value="/list")
    @Permission(url = qxurl,type = PermissionType.QUERY)
    public Object login(Model model) throws Exception {
        model.addAttribute("users", userService.getUserList());
        model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
        return "page/sys/user/list";
    }

    /**
     * 获取用户角色
     * @return
     */
    @GetMapping(value="/getRole")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.EDIT)
    public Object userRole(String userId) throws Exception {
        return userService.getRole(userId);
    }

    /**
     * 添加用户
     * @return
     */
    @PostMapping(value="/add")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.ADD)
    public Object add(User user) throws Exception {
        return userService.add(user);
    }


    /**
     * 编辑用户
     * @return
     */
    @PostMapping(value="/edit")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.EDIT)
    public Object edit(User user) throws Exception {
        return userService.edit(user);
    }

    /**
     * 编辑用户
     * @return
     */
    @PostMapping(value="/editRole")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.EDIT)
    public Object editRole(UserDTO dto) throws Exception {
        return userService.editRole(dto);
    }

    /**
     * 删除用户
     * @return
     */
    @PostMapping(value="/del")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.DEL)
    public Object del(String userId) throws Exception {
        return userService.del(userId);
    }

}
