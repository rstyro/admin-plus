package com.lrs.core.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.common.constant.ResponseModel;
import com.lrs.common.constant.Result;
import com.lrs.core.admin.entity.Menu;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2018-12-14
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 获取菜单表中最大的id
     * @return
     */
    public long getMaxId();

    /**
     * 获取一级菜单，并添加二级菜单
     * @return
     */
    public List<Menu> getAllMenuList();
    /**
     * 获取所有一级菜单
     * @return
     */
    public List<Menu> getAllParentMenuList();

    /**
     * 通过一级id获取二级菜单
     * @param parentId
     * @return
     */
    public List<Menu> getSubMenuListByParentId(Integer parentId);


    /**
     * 获取二级菜单列表,json格式返回
     * @return
     */
    public Result getSubMenuList(Integer parentId);

    /**
     * 添加菜单
     * @param session
     * @return
     */
    public Result addMenu(Menu menu, HttpSession session);

    /**
     * 删除菜单
     * @return
     */
    public Result delMenu(Integer menuId);

    /**
     * 编辑菜单
     * @return
     */
    public Result editMenu(Menu menu);

    /**
     * 查询菜单
     * @return
     */
    public Result findMenu(Integer menuId);
}
