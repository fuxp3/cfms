package com.design.bs.core.utils;

import com.design.bs.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @description: 用户相关的工具
 **/
@Slf4j
public class UserUtils {
	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		return (User) getSubject().getPrincipal();
	}

	/**
	 * 获取session
	 * 
	 * @return
	 */
	public static Session getSession() {
		return getSubject().getSession();
	}

	/**
	 * 获取token
	 * 
	 * @return
	 */
	public static String getToken() {
		return getSession().getId().toString();
	}

	/**
	 * 获取当前主体
	 * 
	 * @return
	 */
	private static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

}
