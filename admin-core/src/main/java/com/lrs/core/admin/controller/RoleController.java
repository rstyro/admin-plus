package com.lrs.core.admin.controller;


import com.lrs.common.annotation.Permission;
import com.lrs.common.annotation.PermissionType;
import com.lrs.core.base.BaseController;
import com.lrs.core.admin.dto.QxDTO;
import com.lrs.core.admin.entity.Role;
import com.lrs.core.admin.service.IRoleService;
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
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

    private final static String qxurl="admin/role/list";

    @Autowired
    private IRoleService roleService;

    /**
     * 角色列表
     * @return
     */
    @GetMapping(value="/list")
    public Object list(Model model) throws Exception {
        model.addAttribute("roles", roleService.getRolelist());
        return "page/admin/role/list";
    }

    /**
     * 获取权限
     * @return
     */
    @PostMapping(value="/qx")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.ADD)
    public Object qx(QxDTO dto) throws Exception {
        return roleService.getMenu(dto);
    }

    /**
     * 更改角色
     * @return
     */
    @PostMapping(value="/edit")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.EDIT)
    public Object edit(Role role) throws Exception {
        return roleService.edit(role);
    }
    /**
     * 添加角色
     * @return
     */
    @PostMapping(value="/add")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.ADD)
    public Object add(Role role) throws Exception {
        return roleService.add(role,this.getSession());
    }
    /**
     * 删除角色
     * @return
     */
    @GetMapping(value="/del/{roleId}")
    @ResponseBody
    @Permission(url = qxurl,type = PermissionType.DEL)
    public Object del(@PathVariable("roleId") Integer roleId) throws Exception {
        return roleService.del(roleId);
    }
}
