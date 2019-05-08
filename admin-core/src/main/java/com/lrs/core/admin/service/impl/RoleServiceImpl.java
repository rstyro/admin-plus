package com.lrs.core.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.common.constant.ApiResultEnum;
import com.lrs.common.constant.ResponseModel;
import com.lrs.common.constant.Result;
import com.lrs.common.exception.ApiException;
import com.lrs.common.utils.permission.RightsHelper;
import com.lrs.core.admin.dto.QxDTO;
import com.lrs.core.admin.entity.Menu;
import com.lrs.core.admin.entity.Role;
import com.lrs.core.admin.mapper.RoleMapper;
import com.lrs.core.admin.service.IMenuService;
import com.lrs.core.admin.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
@Transactional
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IMenuService menuService;

    @Override
    public List<Role> getRolelist()  throws  Exception{
        List<Role> list = this.list(null);
        System.out.println("list="+list);
        return list;
    }

    @Override
    public Result getMenu(QxDTO dto)  throws  Exception{
        String QXtype = dto.getQx();
        BigInteger qx = null;
        List<Menu> menus =null;
        Role role = this.getById(dto.getRoleId());
        if("rights".equalsIgnoreCase(QXtype)){
            qx = role.getRights();
        }else if("add_qx".equalsIgnoreCase(QXtype)){
            qx = role.getAddQx();
        }else if("del_qx".equalsIgnoreCase(QXtype)){
            qx = role.getDelQx();
        }else if("edit_qx".equalsIgnoreCase(QXtype)){
            qx = role.getEditQx();
        }else if("query_qx".equalsIgnoreCase(QXtype)){
            qx = role.getQueryQx();
        }else{
            throw  new ApiException(ApiResultEnum.FAILED,null);
        }
        menus = menuService.getAllMenuList();
        for(Menu m:menus){
            List<Menu> subm = m.getSubMenu();
            if(subm != null && subm.size() > 0){
                int subNumber = subm.size();
                int index = 0;
                for(Menu sm:subm){
                    boolean ishas = RightsHelper.testRights(qx, sm.getMenuId());
                    System.out.println("qx="+qx+",menu_id="+sm.getMenuId()+",result="+ishas);
                    sm.setHasMenu(ishas);
                    if(ishas){
                        index++;
                    }
                }
                //判断子菜单是否全部选中
                if(subNumber == index){
                    m.setHasMenu(true);
                }
            }
        }
        return Result.ok(menus);
    }

    @Override
    public Result edit(Role role)  throws  Exception{
        System.out.println("======role="+role);
        String idstr = role.getIds();
        if(StringUtils.isNotEmpty(idstr)){
            String[] ids = idstr.split(",");
            BigInteger newRights = RightsHelper.sumRights(ids);
            String qx = role.getQx();
            if("rights".equalsIgnoreCase(qx)){
                role.setRights(newRights);
            }else if("add_qx".equalsIgnoreCase(qx)){
                role.setAddQx(newRights);
            }else if("del_qx".equalsIgnoreCase(qx)){
                role.setDelQx(newRights);
            }else if("edit_qx".equalsIgnoreCase(qx)){
                role.setEditQx(newRights);
            }else if("query_qx".equalsIgnoreCase(qx)){
                role.setQueryQx(newRights);
            }
        }
        this.updateById(role);
        return Result.ok();
    }

    @Override
    public Result add(Role role, HttpSession session) throws  Exception{
        this.save(role);
        return Result.ok();
    }

    @Override
    public Result del(Integer roleId)  throws  Exception{
        this.removeById(roleId);
        return Result.ok();
    }
}
