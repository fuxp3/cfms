package com.design.bs.role.service.impl;

import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.core.utils.UserUtils;
import com.design.bs.menu.entity.Menu;
import com.design.bs.role.entity.Role;
import com.design.bs.role.entity.RoleMenu;
import com.design.bs.role.entity.RoleWithMenu;
import com.design.bs.role.mapper.RoleMapper;
import com.design.bs.role.mapper.RoleMenuMapper;
import com.design.bs.role.service.IRoleMenuService;
import com.design.bs.role.service.IRoleService;
import com.design.bs.user.service.IUserRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 角色服务实现
 **/
@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public List<Role> findUserRole(String usercode) {
        return roleMapper.findUserRole(usercode);
    }

    @Override
    public List<Role> queryList(Role role) {
        Example example = new Example(Role.class);
        role = (null == role) ? new Role() : role;
        Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(role.getName()))
            criteria.andCondition(" name like ", "%" + role.getName() + "%");
        if (StringUtils.isNoneBlank(role.getStatus() + "")) {
            criteria.andEqualTo("status", role.getStatus());
        }
        if (StringUtils.isNoneBlank(role.getRoleType() + "")) {
            criteria.andEqualTo("roleType", role.getRoleType());
        }
        criteria.andNotEqualTo("id",1);
        example.setOrderByClause(" create_time desc ");
        return this.findAllByExample(example);

    }

    @Override
    public RoleWithMenu findById(Long id) {
        List<RoleWithMenu> list = roleMapper.findById(id);
        List<Long> menuIds = list.stream().map(RoleWithMenu::getMenuId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list))
            return null;

        RoleWithMenu roleWithMenu = list.get(0);
        roleWithMenu.setMenuIds(menuIds);
        return roleWithMenu;
    }

    @Override
    public void add(RoleWithMenu role) {
        //校验符合条件后再保存
        if (checkName(role.getName(), role.getId())) {
            //保存角色信息
            role.setCreateTime(new Date());
            role.setCreateUser(UserUtils.getCurrentUser().getId());

            //特殊需求考虑用角色类型处理

            this.save(role);

            //保存角色关联的菜单信息
            saveRoleMenu(role);
        }
    }

    @Override
    public String add(Role role) {
        //校验符合条件后再保存
        if (checkName(role.getName(), role.getId())) {
            //保存角色信息
            role.setCreateTime(new Date());
            role.setCreateUser(UserUtils.getCurrentUser().getId());

            //特殊需求考虑用角色类型处理

            this.save(role);
            return "0";
        }else{
            return "角色名称重复";
        }
    }

    @Override
    public boolean checkName(String name, Long id) {
        if (StringUtils.isBlank(name))
            return false;

        Example example = new Example(Role.class);
        Criteria criteria = example.createCriteria();
        criteria.andCondition("lower(name)=", name.toLowerCase());
        if (id != null)
            criteria.andNotEqualTo("id", id);

        return CollectionUtils.isEmpty(this.findAllByExample(example));
    }

    @Override
    public void update(RoleWithMenu role) {
        //删除已有的角色菜单关系数据，再新增关系数据
        Example example = new Example(RoleMenu.class);
        example.createCriteria().andEqualTo("roleId", role.getId());
        roleMenuMapper.deleteByExample(example);
        this.saveRoleMenu(role);
    }

    @Override
    public void delete(List<Long> keys) {
        //系统管理员角色和租户管理员角色不可以删除
        Example example = new Example(Role.class);
        Criteria criteria = example.createCriteria();
        criteria.orEqualTo("id", 1);
        List<Role> roles = this.findAllByExample(example);
        if (null != roles) {
            roles.forEach((role) -> {
                if (keys.contains(role.getId())) {
                    keys.remove(role.getId());
                }
            });
        }
        this.batchDelete(keys, "id", Role.class);
        roleMenuService.deleteRoleMenusByRoleId(keys);
        userRoleService.deleteUserRolesByRoleId(keys);
    }

    /**
     * 保存角色菜单关系
     *
     * @param role
     */
    private void saveRoleMenu(RoleWithMenu role) {
        //menuIds不为null时再遍历添加角色菜单信息
        if (null != role.getMenuIds()) {
            role.getMenuIds().forEach(menuId -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(menuId);
                roleMenu.setRoleId(role.getId());
                roleMenuMapper.insert(roleMenu);
            });
        }
    }

    @Override
    public List<Menu> getRoleMenuAndButtons(List<Long> roleIds) {
        return roleMenuMapper.getRoleMenuAndButtons(roleIds);
    }
}
