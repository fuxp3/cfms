package com.design.bs.user.service;

import com.design.bs.user.entity.UserRole;
import com.design.bs.core.service.BaseService;

import java.util.List;

/**
 * @description: 用户角色服务接口
 **/
public interface IUserRoleService extends BaseService<UserRole> {

    /**
     * 根据用户id，删除所有该用户相关的用户角色关系
     * 删除用户时调用
     * @param userIds
     */
    void deleteUserRolesByUserId(List<Long> userIds);

    /**
     * 根据角色id，删除所有该角色相关的用户角色关系
     * 删除角色时调用
     * @param roleIds
     */
    void deleteUserRolesByRoleId(List<Long> roleIds);
}
