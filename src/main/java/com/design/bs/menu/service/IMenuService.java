package com.design.bs.menu.service;

import com.design.bs.core.dto.Tree;
import com.design.bs.core.service.BaseService;
import com.design.bs.menu.entity.Menu;

import java.util.List;

/**
 * @description: 权限服务接口
 **/
public interface IMenuService extends BaseService<Menu> {

    /**
     * 查询用户所有权限
     *
     * @param usercode
     * @return
     */
    List<Menu> findUserPermissions(String usercode);

    /**
     * 查询用户所有资源（菜单和权限）
     *
     * @param username
     * @return
     */
    List<Menu> findUserResources(String username);

    /**
     * 根据查询条件查询所有菜单
     *
     * @param menu
     * @return
     */
    List<Menu> queryList(Menu menu);

    /**
     * 查询菜单和权限组成的树
     *
     * @return
     */
    List<Tree<Menu>> getMenuButtonTree();

    /**
     * 查询菜单树
     *
     * @return
     */
    List<Tree<Menu>> getMenuTree();

    /**
     * 查询菜单信息
     *
     * @param id
     * @return
     */
    Menu findById(Long id);

    /**
     * 保存菜单
     *
     * @param menu
     */
    void add(Menu menu);

    /**
     * 检查菜单编码是否重复
     * 新增菜单时id为NULL
     *
     * @param name
     * @param id
     * @return
     */
    boolean checkCode(String name, Long id);

    /**
     * 批量删除菜单
     * 注意：
     * 1、需要删除菜单角色关系数据；
     * 2、需要将本菜单的所有子菜单"升级"至一级菜单
     *
     * @param ids
     */
    void delete(List<Long> ids);

}
