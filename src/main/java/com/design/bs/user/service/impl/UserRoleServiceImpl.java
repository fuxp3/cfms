package com.design.bs.user.service.impl;

import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.user.entity.UserRole;
import com.design.bs.user.mapper.UserRoleMapper;
import com.design.bs.user.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void deleteUserRolesByUserId(List<Long> userIds) {
        this.batchDelete(userIds, "userId", UserRole.class);
    }

    @Override
    public void deleteUserRolesByRoleId(List<Long> roleIds) {
        this.batchDelete(roleIds, "roleId", UserRole.class);
    }
}
