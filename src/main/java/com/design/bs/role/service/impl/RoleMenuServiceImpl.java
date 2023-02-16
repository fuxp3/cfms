package com.design.bs.role.service.impl;

import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.role.entity.RoleMenu;
import com.design.bs.role.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: 角色菜单服务实现
 **/
@Service
@Transactional
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenu> implements IRoleMenuService {

    @Override
    public void deleteRoleMenusByRoleId(List<Long> keys) {
        batchDelete(keys, "roleId", RoleMenu.class);
    }

    @Override
    public void deleteRoleMenusByMenuId(List<Long> ids) {
        this.batchDelete(ids, "menuId", RoleMenu.class);
    }
}
