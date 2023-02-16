package com.design.bs.role.mapper;

import com.design.bs.core.basemapper.MyMapper;
import com.design.bs.menu.entity.Menu;
import com.design.bs.role.entity.RoleMenu;

import java.util.List;

/**
 * @description: 角色菜单关系mapper
 **/
public interface RoleMenuMapper extends MyMapper<RoleMenu> {

	 /**
     * 根据角色id数组获取对应的菜单集合
     * @param roleIds
     * @return
     */
    List<Menu> getRoleMenuAndButtons(List<Long> roleIds);
}
