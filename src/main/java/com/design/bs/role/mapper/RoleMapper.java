package com.design.bs.role.mapper;

import com.design.bs.core.basemapper.MyMapper;
import com.design.bs.role.entity.Role;
import com.design.bs.role.entity.RoleWithMenu;

import java.util.List;

/**
 * @description: 角色mapper
 **/
public interface RoleMapper extends MyMapper<Role> {

    List<Role> findUserRole(String usercode);

    List<RoleWithMenu> findById(Long id);
}
