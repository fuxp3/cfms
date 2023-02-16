package com.design.bs.role.service;

import com.design.bs.role.entity.RoleMenu;
import com.design.bs.core.service.BaseService;

import java.util.List;

/**
 * @description: 角色菜单服务接口
 **/
public interface IRoleMenuService extends BaseService<RoleMenu> {

    /**
     * 根据角色id，删除所有该角色相关的角色菜单关系
     * 删除角色时调用
     * @param roleIds
     */
    void deleteRoleMenusByRoleId(List<Long> roleIds);

    /**
     * 根据菜单id，删除所有该菜单相关的角色菜单关系
     * 删除菜单时调用
     * @param menuIds
     */
    void deleteRoleMenusByMenuId(List<Long> menuIds);
}
