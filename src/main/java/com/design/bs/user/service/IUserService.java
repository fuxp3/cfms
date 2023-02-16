package com.design.bs.user.service;

import com.design.bs.user.entity.User;
import com.design.bs.user.entity.UserWithRole;
import com.design.bs.core.dto.Response;
import com.design.bs.core.dto.Tree;
import com.design.bs.core.service.BaseService;
import com.design.bs.menu.entity.Menu;

import java.util.List;

public interface IUserService extends BaseService<User> {

    /**
     * 根据用户名查询用户信息
     * @param usercode
     * @return
     */
    User findByUsername(String usercode);

    /**
     * 根据用户ID查询用户信息
     * 用户信息中包含了用户拥有的角色信息
     * @param id
     * @return
     */
    UserWithRole findById(Long id);

    /**
     * 根据用户名查询用户拥有的菜单
     * @param username
     * @return
     */
    List<Tree<Menu>> getMenuButton(String username);
    
    /**
     * 获取登陆人菜单
     * @return
     */
    List<Menu> getMenus(String username);

    /**
     * 根据查询条件查询用户信息
     * @param user 封装了查询条件
     * @return
     */
    List<User> queryList(User user);

    /**
     * 新增用户
     * 注意：一般来说新增用户的时候，除了需要保存用户基本信息外还会保存用户拥有的角色信息，
     * 所以这里参数类型是 UserWithRole
     * @param user
     */
    void add(UserWithRole user);

    /**
     * 检查用户名是否已经存在
     * 用在新增用户时对用户名的唯一性校验
     * @param usercode
     * @return
     */
    boolean checkUsername(String usercode);
    
    /**
     * 校验当前用户密码是否正确
     * @param password
     * @return
     */
    boolean checkPassword(String password);

    /**
     * 更新用户信息
     * 与新增用户操作类似，会涉及到角色的变更
     * @param user
     */
    void update(UserWithRole user);
    
    /**
     * 个人中心更新用户信息
     * @param user
     */
    void pcupdate(User user);

    /**
     * 批量删除用户
     * @param keys 用户主键ID组成的List
     */
    void delete(List<Long> keys);

    /**
     * 更新密码
     * @param user
     */
    void updatePassword(User user);
    
    /**
     * 重置密码
     * @param userId
     */
    String resetPassword(Long userId);
    
    /**
     * 发送邮件
     */
    void sendMail(Long userId,String password);
    
    /**
     * 判断用户是否拥有权限标识
     * @param token
     * @param permission
     * @return
     */
    Response<String> hasPermission(String token, String permission);
}
