package cn.com.studyshop.conf;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.web.filter.DelegatingFilterProxy;

import cn.com.studyshop.realm.ShiroRealm;

/**
 * shiro配置
 * 
 * @author LIU
 *
 */
@Configuration
public class ShiroConfig {

	@Bean
	public RedisSessionDAO sessionDAO() {
		return new RedisSessionDAO();
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public RedisCacheManager redisCacheManager() {
		return new RedisCacheManager();
	}

	@Bean
	public SimpleCookie sessionIdCookie() {
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("oauth_js_id");
		return simpleCookie;
	}

	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		System.out.println("redis-session配置正常加载::::" + sessionDAO());
		sessionManager.setSessionDAO(sessionDAO());
		sessionManager.setGlobalSessionTimeout(600000); // 过期时间10分钟 授权等记录信息
		sessionManager.setCacheManager(redisCacheManager());
		sessionManager.setSessionIdCookieEnabled(true); // 私有cookie
		sessionManager.setSessionIdCookie(sessionIdCookie());
		return sessionManager;
	}

	// @Bean(name = "hashedCredentialsMatcher")
	// public HashedCredentialsMatcher hashedCredentialsMatcher() {
	// HashedCredentialsMatcher credentialsMatcher = new
	// HashedCredentialsMatcher();
	// credentialsMatcher.setHashAlgorithmName("MD5");
	// credentialsMatcher.setHashIterations(2);
	// credentialsMatcher.setStoredCredentialsHexEncoded(true);
	// return credentialsMatcher;
	// }

	@Bean
	@Scope("singleton")
	public PasswordService passwordService() {
		DefaultPasswordService passwordService = new DefaultPasswordService();
		DefaultHashService defaultHashService = new DefaultHashService();
		passwordService.setHashService(defaultHashService);
		defaultHashService.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME); // md5加密
		// defaultHashService.setPrivateSalt(new
		// SimpleByteSource(UserConstant.SALT)); //私钥salt 不要
		defaultHashService.setHashIterations(1024);
		defaultHashService.setGeneratePublicSalt(false); // 公钥salt不要
		return passwordService;
	}

	@Bean
	public CredentialsMatcher credentialsMatcher() {
		PasswordMatcher passwordMatcher = new PasswordMatcher();
		passwordMatcher.setPasswordService(passwordService());
		return passwordMatcher;
	}

	@Bean(name = "shiroRealm")
	@DependsOn("lifecycleBeanPostProcessor")
	public ShiroRealm shiroRealm() {
		ShiroRealm realm = new ShiroRealm();
		realm.setCredentialsMatcher(credentialsMatcher());
		return realm;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// DefaultWebSessionManager sessionManager = new
		// DefaultWebSessionManager();
		// sessionManager.setGlobalSessionTimeout(28800000); // 8 hours
		securityManager.setSessionManager(sessionManager()); // session control
		securityManager.setCacheManager(redisCacheManager());// cache control
		securityManager.setRealm(shiroRealm());
		System.out.println("shiro-spring配置正常加载!");
		return securityManager;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE,
				DispatcherType.ERROR);
		return filterRegistration;
	}

	// 权限配置设定
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());

		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl("/login");

		shiroFilterFactoryBean.setFilters(filters);

		Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();
		filterChainDefinitionManager.put("/logout", "logout");
		// filterChainDefinitionManager.put("/user/**",
		// "authc,roles[ROLE_USER]");
		// filterChainDefinitionManager.put("/users/**",
		// "authc,roles[ROLE_USER]");
		filterChainDefinitionManager.put("/events/**", "authc,roles[ROLE_ADMIN]");
		filterChainDefinitionManager.put("/suer/edit/**", "authc,perms[user:edit]");
		filterChainDefinitionManager.put("/**", "anon");// 无校验
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);
		shiroFilterFactoryBean.setSuccessUrl("/");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		return shiroFilterFactoryBean;
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
		defaultAAP.setProxyTargetClass(true);
		return defaultAAP;
	}

	/**
	 * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
	 * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aASA = new AuthorizationAttributeSourceAdvisor();
		aASA.setSecurityManager(securityManager());
		return aASA;
	}
}
