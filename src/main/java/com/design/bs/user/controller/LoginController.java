package com.design.bs.user.controller;

import com.design.bs.core.dto.Response;
import com.design.bs.core.enums.StatusCode;
import com.design.bs.core.utils.UserUtils;
import com.design.bs.menu.service.IMenuService;
import com.design.bs.role.service.IRoleService;
import com.design.bs.user.entity.User;
import com.design.bs.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * @description: 登陆控制
 **/
@Controller
@Slf4j
public class LoginController {

	private Boolean verificationCode = false;//是否校验验证码          true校验          false不校验

	private Boolean allowedMultiplelogin=true;//是否允许一个账号多处登录          true允许          false不允许

    private Boolean isEncodePassword=false;

	@Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    private static final String CODE_KEY = "gifCode";

    /**
     * 登录认证失败
     * @return
     */
    @ResponseBody
    @GetMapping("unlogin")
    public Response unlogin(){
        return new Response(StatusCode.USER_LOGIN_REQUIRED);
    }

    /**
     * 权限认证失败
     * @return
     */
    @ResponseBody
    @GetMapping("unauthorized")
    public Response unauthorized(){
        return new Response(StatusCode.USER_PERMISSION_REQUIRED);
    }


    @ResponseBody
    @PostMapping("/login")
    //@Log("登录")
    public Map<String,Object> login(@RequestBody @Valid User user, BindingResult bindingResult, HttpSession session) {
        Map<String,Object> map=new HashMap<String,Object>();
        if(bindingResult.hasErrors()){
            map.put("success", false);
            map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
            return map;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null)
                subject.logout();

            SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getUsercode(), user.getPassword()));

            //生产环境移除当前账号的其它session  是否允许一个账号多处登录          true允许          false不允许
        	if(null==allowedMultiplelogin || !allowedMultiplelogin) {
            	List<Session> loginedList = getLoginedSession(subject);
                for (Session lsession : loginedList) {
                	lsession.stop();
                }
            }
            map.put("success", true);
            map.put("token", UserUtils.getToken());
        } catch (UnknownAccountException uae) {
            map.put("success", false);
            map.put("errorInfo", "用户帐号或密码不正确");
        } catch (IncorrectCredentialsException ice) {
            map.put("success", false);
            map.put("errorInfo", "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            map.put("success", false);
            map.put("errorInfo", "用户帐号被锁定");
        } catch (AuthenticationException ae) {
            map.put("success", false);
            map.put("errorInfo", "登录出错");
        }
        return map;
    }

    @ResponseBody
    @GetMapping("/login")
    public Map<String,Object> login(String imageCode, String usercode, String password, BindingResult bindingResult, HttpSession session) {
        Map<String,Object> map=new HashMap<String,Object>();
        if(Strings.isEmpty(imageCode)){
            map.put("success", false);
            map.put("errorInfo", "请输入验证码！");
            return map;
        }
        if(session.getAttribute("checkcode")==null){
            map.put("success", false);
            map.put("errorInfo", "验证码已过期！");
            return map;
        }
        if(!session.getAttribute("checkcode").equals(imageCode)){
            map.put("success", false);
            map.put("errorInfo", "验证码输入错误！");
            return map;
        }
        if(bindingResult.hasErrors()){
            map.put("success", false);
            map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
            return map;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null)
                subject.logout();

            SecurityUtils.getSubject().login(new UsernamePasswordToken(usercode, password));

            //生产环境移除当前账号的其它session  是否允许一个账号多处登录          true允许          false不允许
            if(null==allowedMultiplelogin || !allowedMultiplelogin) {
                List<Session> loginedList = getLoginedSession(subject);
                for (Session lsession : loginedList) {
                    lsession.stop();
                }
            }
            map.put("success", true);
        } catch (UnknownAccountException uae) {
            map.put("success", false);
            map.put("errorInfo", "用户帐号或密码不正确");
        } catch (IncorrectCredentialsException ice) {
            map.put("success", false);
            map.put("errorInfo", "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            map.put("success", false);
            map.put("errorInfo", "用户帐号被锁定");
        } catch (AuthenticationException ae) {
            map.put("success", false);
            map.put("errorInfo", "登录出错");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/logout")
    //@Log("登出")
    public Response<String> logout() {
        SecurityUtils.getSubject().logout();
        return new Response<String>(StatusCode.OK);
    }
    
    /**
     * 
     * @param currentUser
     * @return
     */
    private List<Session> getLoginedSession(Subject currentUser){
    	//获取所有活跃的session
    	Collection<Session> list = ((DefaultSessionManager) ((DefaultSecurityManager) SecurityUtils
                .getSecurityManager()).getSessionManager()).getSessionDAO()
                .getActiveSessions();
    	//当前账号的活跃session
    	List<Session> loginedList = new ArrayList<Session>();
    	User loginUser = (User)currentUser.getPrincipal();
    	for(Session session : list) {
    		 Subject s = new Subject.Builder().session(session).buildSubject();
    		 if (s.isAuthenticated()) {
    			 User user = (User) s.getPrincipal();
    			 if (user.getUsercode().equalsIgnoreCase(loginUser.getUsercode())) {
    				 if (!session.getId().equals(currentUser.getSession().getId())) {
    					 loginedList.add(session);
    				 }
    			 }
    		 }
    	}
    	return loginedList;
    }
}
