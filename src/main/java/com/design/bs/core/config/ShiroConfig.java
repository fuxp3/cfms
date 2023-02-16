package com.design.bs.core.config;

import com.design.bs.core.realm.MyAuthRealm;
import com.design.bs.core.utils.SysConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: shiro框架配置
 **/
@Configuration
//@AutoConfigureAfter(value = ShiroLifecycleBeanPostProcessorConfig.class)
public class ShiroConfig {

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 设置登陆url
		shiroFilterFactoryBean.setLoginUrl("/login.html");

		// 登录成功后跳转的 url
		shiroFilterFactoryBean.setUnauthorizedUrl("/");

		// 设置未授权url
		shiroFilterFactoryBean.setUnauthorizedUrl("unauthorized");

		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

		// 配置退出过滤器，其中具体的退出代码 Shiro已经替我们实现了
		// 自己实现登出需要注掉默认的登出过滤器
		// filterChainDefinitionMap.put(footConfig.getShiro().getLogoutUrl(),
		// "logout");

		// 设置免认证 url
		String anon = "/login,/drawImage,/javaex/**,/css/**,/img/**,/js/**";
		String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(anon, ",");
		for (String url : anonUrls) {
			filterChainDefinitionMap.put(url, "anon");
		}

		// TODO 除上以外所有 url都必须认证通过才可以访问，未通过认证自动访问 LoginUrl
		filterChainDefinitionMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public Realm realm(HashedCredentialsMatcher matcher) {
		MyAuthRealm realm = new MyAuthRealm();
		realm.setCredentialsMatcher(matcher);
		return realm;
	}

	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// securityManager.setRememberMeManager(rememberMeManager());
		securityManager.setSessionManager(sessionManager());
		// securityManager.setCacheManager(cacheManager());
		securityManager.setRealm(realm(hashedCredentialsMatcher()));
		return securityManager;
	}

	/*
	 * @Bean public SimpleCookie rememberCookie() { SimpleCookie simpleCookie =
	 * new SimpleCookie("rememberMe"); return simpleCookie; }
	 */

	/*
	 * @Bean public CookieRememberMeManager rememberMeManager() {
	 * CookieRememberMeManager cookieRememberMeManager = new
	 * CookieRememberMeManager();
	 * cookieRememberMeManager.setCookie(rememberCookie());
	 * cookieRememberMeManager.setCipherKey(Base64.decode(
	 * "4AvVhmFLUs0KTA3Kprsdag==" )); return cookieRememberMeManager; }
	 */

	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName(SysConstant.ALGORITHNAME);
		credentialsMatcher.setHashIterations(SysConstant.HASHNUM);
		return credentialsMatcher;
	}

	/**
	 * 注入AuthorizationAttributeSourceAdvisor的bean @RequiresPermissions没有生效
	 * 所以加了一个DefaultAdvisorAutoProxyCreator的bean
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	/**
	 * 开启Shiro的注解支持 比如：@RequireRoles @RequireUsers
	 *
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	public SessionManager sessionManager() {
		MySessionManager mySessionManager = new MySessionManager();
		mySessionManager.setSessionIdUrlRewritingEnabled(true);
		mySessionManager.setGlobalSessionTimeout(86400000);
		return mySessionManager;
	}
}
