package com.lrs.core.system.mapper;

import com.lrs.core.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lrs.core.system.vo.RoleMenuTreeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rstyro
 * @since 2023-12-01
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<RoleMenuTreeVo> getRoleMenuList(@Param("roleId") Long roleId,@Param("parentId")  Long parentId);
}
