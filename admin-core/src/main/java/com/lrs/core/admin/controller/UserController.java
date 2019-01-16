package com.lrs.core.admin.controller;


import com.lrs.common.annotation.Permission;
import com.lrs.common.annotation.PermissionType;
import com.lrs.common.constant.Const;
import com.lrs.core.admin.dto.UserDTO;
import com.lrs.core.admin.entity.User;
import com.lrs.core.admin.service.IUserService;
import com.lrs.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {
    private final static String qxurl="admin/user/list";

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
        return "page/admin/user/list";
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
