package com.design.bs.user.controller;

import com.design.bs.menu.service.IMenuService;
import com.design.bs.role.entity.Role;
import com.design.bs.role.service.IRoleService;
import com.design.bs.user.entity.User;
import com.design.bs.user.entity.UserMessage;
import com.design.bs.user.entity.UserWithRole;
import com.design.bs.core.dto.QueryPageRequest;
import com.design.bs.core.dto.QueryPageResult;
import com.design.bs.core.dto.Response;
import com.design.bs.core.dto.Tree;
import com.design.bs.core.enums.StatusCode;
import com.design.bs.core.utils.PageUtils;
import com.design.bs.core.utils.PasswordHelper;
import com.design.bs.core.utils.TreeUtils;
import com.design.bs.core.utils.UserUtils;
import com.design.bs.menu.entity.Menu;
import com.design.bs.user.mapper.UserMapper;
import com.design.bs.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 用户服务
 **/
@RestController
@Slf4j
@RequestMapping("/system/user")
public class UserController {

	@Autowired
    private UserMapper userMapper;
	
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IRoleService roleService;
    
    @Autowired
    private IMenuService menuService;

    @Autowired
    private PasswordHelper passwordHelper;

    @GetMapping("/info")
    public Response<UserMessage> info() {
    	User user = UserUtils.getCurrentUser();
    	if(null!=user) {
    		user = userMapper.selectByPrimaryKey(user.getId());
    		 //获取用户角色
            List<Role> roleList = roleService.findUserRole(user.getUsercode());
            Set<String> roleSet = null;
            if(null!=roleList && !roleList.isEmpty() && null!=roleList.get(0)) {
            	roleSet = roleList.stream().map(Role::getName).collect(Collectors.toSet());
            }
            //获取用户权限
            List<Menu> menuList = menuService.findUserPermissions(user.getUsercode());
            //获取用户权限按钮名称
            Map<String,Object> btnName = new LinkedHashMap<String,Object>();
            Set<String> permSet = null;
            if(null!=menuList && !menuList.isEmpty() && null!=menuList.get(0)) {
            	permSet = menuList.stream().map(Menu::getPerms).collect(Collectors.toSet());
            	btnName = menuList.stream().collect(Collectors.toMap(Menu::getPerms, Menu::getName, (key1, key2) -> key2));
            }

            user.setPassword("");
            user.setSalt("");
            return new Response<UserMessage>(StatusCode.OK, new UserMessage(UserUtils.getToken(),roleSet,permSet,btnName,user));
    	}
        return new Response<UserMessage>(StatusCode.SYSTEM_ERROR,new UserMessage());
    }

    /**
     * 有树形结构的菜单json
     * @param
     * @return
     */
    @RequestMapping("/menubutton")
    public List<Tree<Menu>> getMenuButton() {
        User user = UserUtils.getCurrentUser();
        return userService.getMenuButton(user.getUsercode());
    }

    @RequestMapping("/menusTree")
    public List<Tree<Menu>> getUserMenusTree() {
        List<Menu> menus = userService.getMenus(UserUtils.getCurrentUser().getUsercode());
        List<Tree<Menu>> treeList = new ArrayList<>();
        menus.forEach(menu -> {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getId());
            tree.setParentId(menu.getParentId());
            tree.setText(menu.getName());
            tree.setUrl(menu.getUrl());
            tree.setIconCls(menu.getIcon());
            Map<String,Object> attributes = new HashMap<>();
            attributes.put("url",menu.getUrl());
            tree.setAttributes(attributes);
            treeList.add(tree);
        });

        return TreeUtils.build(treeList);
    }
    
    /**
     * 没有树形结构的菜单json
     * @return
     */
    @GetMapping("/menus")
    public Response<List<Menu>> getUserMenus() {
    	Response<List<Menu>> response = new Response<>(StatusCode.OK);
    	if(null!=UserUtils.getCurrentUser()) {
    		response.setData(userService.getMenus(UserUtils.getCurrentUser().getUsercode()));
    	}else {
    		response.setCode(StatusCode.USER_LOGIN_REQUIRED.getCode());
    	}
        return response;
    }

    @PostMapping("/list")
    @RequiresPermissions("user:list")
    public QueryPageResult queryList(int page,int rows,String name,Long department) {
        QueryPageRequest<User> queryPageRequest = new QueryPageRequest();
        User user = new User();
        user.setName(name);
        user.setDepartment(department);
        queryPageRequest.setPage(page);
        queryPageRequest.setRows(rows);
        queryPageRequest.setData(user);
        return PageUtils.queryPage(queryPageRequest, () -> userService.queryList(queryPageRequest.getData()));
    }

    @PostMapping("/listAll")
    public List<User> queryList(@RequestParam(value = "select",required = false)Integer select, User user){
        List<User> list = userService.queryList(user);
        if (null!=select && select>0){
            User u = new User();
            u.setId(-1l);
            u.setName("全部");
            u.setUsercode("全部");
            list.add(0,u);
        }
        return list;
    }

    @RequiresPermissions(value= {"user:detail","user:update"},logical=Logical.OR)
    @GetMapping("/detail/{id}")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/add")
    //@RequiresPermissions("user:add")
    //@Log("新增用户")
    public String add(User user) {
        synchronized (this){
            //监测用户名是否重复
            if(userService.checkUsername(user.getUsercode())) {
                // save user
                user.setStatus(1);
                user.setCreateTime(new Date());
                passwordHelper.encryptPassword(user);
                user.setCreateUser(user.getCreateUser() == null ? UserUtils.getCurrentUser().getId() : user.getCreateUser());

                userMapper.insertSelective(user);
                return "0";
            }else{
                return "用户名称重复";
            }
        }
    }

    @PostMapping("/checkUsername")
    public Boolean checkName(@RequestBody Map<String,String> map) {
        return userService.checkUsername(map.get("usercode"));
    }
    
    @PostMapping("/checkPassword")
    public Boolean checkPassword(@RequestBody User user) {
    	return userService.checkPassword(user.getPassword());
    }

    @PostMapping("/update")
    //@RequiresPermissions("user:update")
    //@Log("更新用户")
    public String update(@RequestParam("id")Long id, UserWithRole user) {
        synchronized (this){
            user.setId(id);
            //更新用户信息
            user.setPassword(null); //这里设置为null，结合 updateNotNull 方法，表示不更新密码
            user.setUpdateTime(new Date());
            user.setSalt(null);
            user.setUpdateUser(UserUtils.getCurrentUser().getId());
            userService.updateNotNull(user);
            return "0";
        }
    }

    @PostMapping("/updateUserRole")
    public String update(Long userId,String roleIds) {
        synchronized (this){
            UserWithRole user = new UserWithRole();
            user.setId(userId);;
            if (StringUtils.isNotBlank(roleIds)){
                user.setRoleIds(Arrays.stream(roleIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            }else {
                user.setRoleIds(new ArrayList<>());
            }
            userService.update(user);
            return "0";
        }
    }

    @PostMapping("/pcupdate")
    //@Log("更新用户")
    public Boolean pcupdate(@RequestBody User user) {
    	userService.pcupdate(user);
    	return true;
    }

    @PostMapping("/delete")
    //@RequiresPermissions("user:delete")
    //@Log("删除用户")
    public Boolean delete(Long id) {
        userService.delete(Arrays.asList(id));
        return true;
    }

    @PostMapping("/changeAvatar")
    //@Log("修改用户头像")
    public Boolean changeAvatar(String url) {
        User user = UserUtils.getCurrentUser();
        user.setAvatar(url);
        userService.updateNotNull(user);
        return true;
    }

    @PostMapping("/updatePassword")
    //@Log("更新用户密码")
    public Boolean updatePassword(@RequestBody User user) {
        userService.updatePassword(user);
        return true;
    }

    /**
     * isms 系统更新密码
     * @param user
     * @return
     */
    @PostMapping("/modifyPassword")
    public Response modifyPassword(User user){
        userService.updatePassword(user);
        return new Response(StatusCode.OK);
    }
    
    @PostMapping("/resetPassword/{userId}")
    @RequiresPermissions("user:resetPassword")
    //@Log("重置用户密码")
    public Boolean resetPassword(@PathVariable Long userId) {
    	String password = userService.resetPassword(userId);
    	//异步发送邮件
    	userService.sendMail(userId,password);
    	return true;
    }

    @PostMapping("/achievementRank")
    public List<User> achievementRank(){
        List<User> userList = new ArrayList<>();
        User user = UserUtils.getCurrentUser();
        if(null!=user) {
            user = userMapper.selectByPrimaryKey(user.getId());
            //获取用户权限
            List<Menu> menuList = menuService.findUserPermissions(user.getUsercode());
            //获取用户权限按钮名称
            Map<String, Object> btnName = new LinkedHashMap<String, Object>();
            Set<String> permSet = null;
            if (null != menuList && !menuList.isEmpty() && null != menuList.get(0)) {
                permSet = menuList.stream().map(Menu::getPerms).collect(Collectors.toSet());
                btnName = menuList.stream().collect(Collectors.toMap(Menu::getPerms, Menu::getName, (key1, key2) -> key2));
            }

            //判断是否有查看所有员工当月业绩排行榜权限
            if(permSet.contains("rank:all")){
                userList = userMapper.achievementRankAll(monthFirstDay(),monthLastDay());
            }else{
                //如果有部门，默认查看本部门员工当月业绩排行榜权限
                if (null!=user.getDepartment() && user.getDepartment()>0){
                    userList = userMapper.achievementRank(user.getDepartment(),monthFirstDay(),monthLastDay());
                }
            }
        }
        return userList;
    }

    //获取当前月第一天的日期
    public Date monthFirstDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    //获取当前月最后一天的日期
    public Date monthLastDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
}
