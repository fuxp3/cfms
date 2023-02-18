package com.design.bs.user.service.impl;

import com.design.bs.core.dto.Response;
import com.design.bs.core.dto.Tree;
import com.design.bs.core.enums.StatusCode;
import com.design.bs.core.exception.BusinessException;
import com.design.bs.core.service.impl.BaseServiceImpl;
import com.design.bs.core.utils.PasswordHelper;
import com.design.bs.core.utils.TreeUtils;
import com.design.bs.core.utils.UserUtils;
import com.design.bs.menu.entity.Menu;
import com.design.bs.menu.mapper.MenuMapper;
import com.design.bs.menu.service.IMenuService;
import com.design.bs.role.mapper.RoleMapper;
import com.design.bs.user.entity.User;
import com.design.bs.user.entity.UserRole;
import com.design.bs.user.entity.UserWithRole;
import com.design.bs.user.mapper.UserMapper;
import com.design.bs.user.mapper.UserRoleMapper;
import com.design.bs.user.service.IUserRoleService;
import com.design.bs.user.service.IUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 用户服务接口现实
 **/
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private IUserRoleService userRoleService;
    
    private IMenuService menuService;

    @Override
    public User findByUsername(String usercode) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("usercode", usercode);

        List<User> list = this.findAllByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public UserWithRole findById(Long id) {
        List<UserWithRole> list = userMapper.findById(id);
        if (list == null || list.isEmpty()) {
            return null;
        }
        //用户拥有的角色
        List<Long> roleIds = list.stream().map(UserWithRole::getRoleId).collect(Collectors.toList());
        UserWithRole userWithRole = list.get(0);
        userWithRole.setRoleIds(roleIds);
        
        //用户拥有的菜单
        List<Menu> menuAndButtons = userWithRole.getMenuAndButtons();
        if(menuAndButtons.isEmpty() || null==menuAndButtons.get(0)) {
        	userWithRole.setMenuAndButtons(new ArrayList<Menu>());
        }
        return userWithRole;
    }

    
    @Override
    public List<Tree<Menu>> getMenuButton(String username) {
        //获取用户资源列表和角色列表
        List<Menu> menus = menuMapper.getMenuButton(username);

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
    
    @Override
    public List<Menu> getMenus(String username){
    	return menuMapper.findUserMenus(username);
    }

    @Override
    public List<User> queryList(User user) {
        Example example = new Example(User.class);
        example.createCriteria().andLike("usercode", "%" + user.getName() + "%")
                .andLike("name", "%" + user.getName() + "%");
        example.orderBy("createTime").desc();
        return userMapper.selectByExample(example);
    }
    
    @Override
    public void add(UserWithRole user) {
    	//监测用户名是否重复
    	if(checkUsername(user.getUsercode())) {
    		 // save user
            user.setCreateTime(new Date());
            passwordHelper.encryptPassword(user);
            user.setCreateUser(user.getCreateUser() == null ? UserUtils.getCurrentUser().getId() : user.getCreateUser());
            
            this.save(user);

            //save user role relations
            saveUserRole(user);
    	}
    }

    @Override
    public boolean checkUsername(String usercode) {
        if (StringUtils.isBlank(usercode))
            return false;

        Example example = new Example(User.class);
        example.createCriteria().andCondition("lower(usercode)=", usercode.toLowerCase());
        return CollectionUtils.isEmpty(this.findAllByExample(example));
    }
    
    @Override
	public boolean checkPassword(String password) {
    	if (StringUtils.isBlank(password))
    		return false;
    	User user = UserUtils.getCurrentUser();
    	if(null!=user) {
    		user=userMapper.selectByPrimaryKey(user.getId());
    		return passwordHelper.getPassword(password, user.getSalt()).equals(user.getPassword());
    	}
		return false;
	}

    @Override
    public void update(UserWithRole user) {
        //更新用户角色信息，先删除用户所有角色，再新增用户角色
        Example example = new Example(UserRole.class);
        example.createCriteria().andEqualTo("userId", user.getId());
        userRoleMapper.deleteByExample(example);
        saveUserRole(user);
    }

    @Override
    public void delete(List<Long> keys) {
		this.batchDelete(keys, "id", User.class);
		userRoleService.deleteUserRolesByUserId(keys);
    }

    @Override
    public void updatePassword(User user) {
        User cuser = (User) SecurityUtils.getSubject().getPrincipal();
        if(checkPassword(user.getOldpwd())) {
        	Example example = new Example(User.class);
            example.createCriteria().andEqualTo("usercode", cuser.getUsercode());
            cuser.setPassword(user.getPassword());
            passwordHelper.encryptPassword(cuser);
            userMapper.updateByExampleSelective(cuser, example);
        }else {
            throw new BusinessException(StatusCode.SYSTEM_ERROR,"原密码错误！");
        }
    }
    
    @Override
	public String resetPassword(Long userId) {
    	User user = userMapper.selectByPrimaryKey(userId);
    	String password ="";
    	if(null!=user) {
    		password = passwordHelper.getResetPassword(6);
    		user.setPassword(password);
    		passwordHelper.encryptPassword(user);
    		userMapper.updateByPrimaryKeySelective(user);
    	}
    	return password;
	}
    
	@Override
	@Async
	public void sendMail(Long userId, String password) {
		/*User user = userMapper.selectByPrimaryKey(userId);
		String[] to = null;
    	StringBuilder subject = null;
    	StringBuilder content = null;
		if(null!=user) {
			EmailVO mail = new EmailVO();
			//发送给谁
    		to=new String[] { user.getEmail() };
    		subject=new StringBuilder();
    		//主题
    		subject.append("密码重置");
    		
    		//内容
    		content = new StringBuilder();
    		content.append("用户名：").append(user.getUsercode()).append("\r\n");
    		content.append("新密码：").append(password).append("\r\n");
    		content.append("请及时登录系统更新密码").append("\r\n");

    		
    		mail.setTo(to);
    		mail.setSubject(subject.toString());
    		mail.setContent(content.toString());

    		emailService.sendEmail(mail);
		}*/
	}

    /**
     * 保存用户的角色信息
     * @param user
     */
    private void saveUserRole(UserWithRole user) {
    	if(null!=user.getRoleIds() && !user.getRoleIds().isEmpty() && null!=user.getRoleIds().get(0)) {
    		user.getRoleIds().forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            });
    	}
    }

	@Override
	public void pcupdate(User user) {
		User currentUser = UserUtils.getCurrentUser();
		if(null!=currentUser) {
			User tempUser = new User();
			tempUser.setId(currentUser.getId());
			tempUser.setName(user.getName());
			tempUser.setSex(user.getSex());
			tempUser.setAvatar(user.getAvatar());
			tempUser.setPhone(user.getPhone());
			tempUser.setEmail(user.getEmail());
			userMapper.updateByPrimaryKeySelective(tempUser);
		}
	}

	@Override
	public Response<String> hasPermission(String token, String permission) {
		Response<String> response = null;
		try {
			List<Session> allActiveSessionList = getAllActiveSession();
			//当前session
			Session currentSession = null;
			for(Session session : allActiveSessionList) {
				if(session.getId().toString().equals(token)) {
					currentSession = session;
					break;
				}
			}
			if(null!=currentSession) {
				User currentUser = (User) new Subject.Builder().session(currentSession).buildSubject().getPrincipal();
				 
				//获取用户权限
	            List<Menu> menuList = menuService.findUserPermissions(currentUser.getUsercode());
	            //获取用户权限按钮名称
	            Set<String> permSet = null;
	            if(null!=menuList && !menuList.isEmpty() && null!=menuList.get(0)) {
	            	permSet = menuList.stream().map(Menu::getPerms).collect(Collectors.toSet());
	            }
	            
	            if(null == permission || permSet.contains(permission)) {
	            	response = new Response<String>(StatusCode.TOKEN_HAS_PERMISSION);
	            }else {
	            	response = new Response<String>(StatusCode.TOKEN_NOT_HAS_PERMISSION);
	            }
			}else {
				response=new Response<String>(StatusCode.TOKEN_INVALID);
			}
		}catch(Exception e) {
			response=new Response<String>(StatusCode.TOKEN_AUTH_EXCEPTION);
		}
		response.setData("OpenApi认证");
		return response;
	}

	/**
     * 
     * @param
     * @return
     */
    private List<Session> getAllActiveSession(){
    	//获取所有活跃的session
    	Collection<Session> list = ((DefaultSessionManager) ((DefaultSecurityManager) SecurityUtils
                .getSecurityManager()).getSessionManager()).getSessionDAO()
                .getActiveSessions();
    	//所有账号的活跃session
    	List<Session> loginedList = new ArrayList<Session>();
    	for(Session session : list) {
    		Subject s = new Subject.Builder().session(session).buildSubject();
    		if (s.isAuthenticated()) {
    			loginedList.add(session);
    		}
    	}
    	return loginedList;
    }
}
