package com.design.bs.core.realm;

import com.design.bs.menu.service.IMenuService;
import com.design.bs.role.entity.Role;
import com.design.bs.role.service.IRoleService;
import com.design.bs.user.entity.User;
import com.design.bs.user.service.IUserService;
import com.design.bs.core.enums.StatusEnums;
import com.design.bs.menu.entity.Menu;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyAuthRealm extends AuthorizingRealm {
    /**
     * @Lazy注解，
     * 延迟Realm实现中Service对象的初始化时间，保证Service实际初始化的时候会被BeanPostProcessor拦截，创建具有事务功能的代理对象
     */
    @Autowired
    @Lazy
    private IUserService userService;

    @Autowired
    @Lazy
    private IRoleService roleService;

    @Autowired
    @Lazy
    private IMenuService menuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null)
            throw new AuthorizationException("principals method argument cannot be null.");

        User user = (User) getAvailablePrincipal(principals);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //获取用户角色
        List<Role> roleList = roleService.findUserRole(user.getUsercode());
        Set<String> roleSet = null;
        if(null!=roleList && !roleList.isEmpty() && null!=roleList.get(0)) {
            roleSet = roleList.stream().map(Role::getName).collect(Collectors.toSet());
        }
        simpleAuthorizationInfo.setRoles(roleSet);

        //获取用户权限
        List<Menu> menuList = menuService.findUserPermissions(user.getUsercode());
        Set<String> permSet = null;
        if(null!=menuList && !menuList.isEmpty() && null!=menuList.get(0)) {
            permSet = menuList.stream().map(Menu::getPerms).collect(Collectors.toSet());
        }
        simpleAuthorizationInfo.setStringPermissions(permSet);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1. 从token中获取输入的用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();

        // Null username is invalid
        if (username == null)
            throw new AccountException("Null username are not allowed");

        // null user is invalid
        User user = userService.findByUsername(username);
        if (user == null)
            throw new UnknownAccountException("No User found by username [" + username + "]");

        //disabled user is invalid
        StatusEnums currentStatusEnums = StatusEnums.getInstance(user.getStatus());
        if (StatusEnums.INVALID == currentStatusEnums)
            throw new DisabledAccountException("the user [" + username + "] is disabled");

        //locked user in invalid
        if (StatusEnums.LOCKED == currentStatusEnums)
            throw new LockedAccountException("the user [" + username + "] is locked");

        /**
         * 交给Shiro进行密码的解密校验
         * 调用SecurityUtils.getSubject().getPrincipal() 遇到类型转换问题，报错 String !=> User
         * 请参考这篇文章：{@link https://blog.csdn.net/qq_35981283/article/details/78634575}
         */
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );
    }
}
