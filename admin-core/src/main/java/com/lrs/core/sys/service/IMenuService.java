package com.lrs.core.sys.service;

import com.lrs.core.entity.ResponseModel;
import com.lrs.core.sys.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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
    public ResponseModel getSubMenuList(Integer parentId);

    /**
     * 添加菜单
     * @param session
     * @return
     */
    public ResponseModel addMenu(Menu menu, HttpSession session);

    /**
     * 删除菜单
     * @return
     */
    public ResponseModel delMenu(Integer menuId);

    /**
     * 编辑菜单
     * @return
     */
    public ResponseModel editMenu(Menu menu);

    /**
     * 查询菜单
     * @return
     */
    public ResponseModel findMenu(Integer menuId);
}
