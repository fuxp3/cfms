package com.design.bs.role.service;

import com.design.bs.role.entity.Role;
import com.design.bs.role.entity.RoleWithMenu;
import com.design.bs.core.service.BaseService;
import com.design.bs.menu.entity.Menu;

import java.util.List;

/**
 * @description: 角色服务接口
 **/
public interface IRoleService extends BaseService<Role> {

    /**
     * 查询用户拥有的角色
     * @param usercode 用户名
     * @return
     */
    List<Role> findUserRole(String usercode);

    /**
     * 根据条件，查询所有角色
     * @param role
     * @return
     */
    List<Role> queryList(Role role);

    /**
     * 查询角色对象，包含角色相关的菜单信息
     * @param id
     * @return
     */
    RoleWithMenu findById(Long id);

    /**
     * 增加角色
     * 同时保存角色相关的菜单信息
     * @param role
     */
    void add(RoleWithMenu role);

    /**
     * 增加角色
     * 同时保存角色相关的菜单信息
     * @param role
     */
    String add(Role role);

    /**
     * 校验角色名是否重复
     * 新增时参数id为null
     * @param name
     * @param id
     * @return
     */
    boolean checkName(String name, Long id);

    /**
     * 更新角色信息，包含角色关联的菜单信息
     * @param role
     */
    void update(RoleWithMenu role);

    /**
     * 批量删除角色
     * 注意：需要同时删除用户角色关系、和角色菜单关系
     * @param keys
     */
    void delete(List<Long> keys);
    
    /**
     * 根据角色id数组获取对应的菜单集合
     * @param roleIds
     * @return
     */
    List<Menu> getRoleMenuAndButtons(List<Long> roleIds);

}
