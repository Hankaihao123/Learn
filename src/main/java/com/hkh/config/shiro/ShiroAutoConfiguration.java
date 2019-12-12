package com.hkh.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.hkh.config.shiro.filter.ShiroLoginFilter;
import com.hkh.config.shiro.realm.UserRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.Data;

@Configuration
//必须是Web环境才生效
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(SecurityManager.class)
@Data
@ConfigurationProperties("shiro")
public class ShiroAutoConfiguration {

	private static final String SHIRO_FILTER = "shiroFilter";
	// 算法
	private String hashAlgorithm = "MD5";
	// 迭代次数
	private int hashIterations = 2;
	// 登录页面
	private String loginUrl = "/index.html";
	// 登出的url
	private String logoutUrl;
	// 不用认证的url
	private String[] anonsUrls;
	// 需要证的url
	private String[] authcUrls;

	// 声明凭证匹配器,密码加盐和迭代次数
	@Bean("credentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName(this.hashAlgorithm);
		credentialsMatcher.setHashIterations(this.hashIterations);
		return credentialsMatcher;
	}

	// 自定义Realm
	@Bean("userRealm")
	public UserRealm userRealm(@Qualifier("credentialsMatcher") CredentialsMatcher credentialsMatcher) {
		UserRealm userRealm = new UserRealm();
		userRealm.setCredentialsMatcher(credentialsMatcher);
		return userRealm;
	}

	// 配置SecurityManager
	@Bean("securityManager")
	public SecurityManager securityManager(@Qualifier("userRealm") UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(userRealm);
		return securityManager;
	}

	@Bean(SHIRO_FILTER)
	public ShiroFilterFactoryBean shiroFilterFactoryBean(
			@Qualifier("securityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		factoryBean.setLoginUrl(loginUrl);
		factoryBean.setUnauthorizedUrl("/404.html");
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		if (null != logoutUrl) {
			filterChainDefinitionMap.put(logoutUrl, "logout");
		}
		if (null != anonsUrls && anonsUrls.length > 0) {
			for (String anon : anonsUrls) {
				filterChainDefinitionMap.put(anon, "anon");
			}
		}
		if (null != authcUrls && authcUrls.length > 0) {
			for (String authc : authcUrls) {
				filterChainDefinitionMap.put(authc, "authc");
			}
		}
		// 将url放进Map集合中
		factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		// 增加shiro的认证过滤器
		factoryBean.getFilters().put("authc", new ShiroLoginFilter());
		return factoryBean;
	}

	// 创建了web.xml中的过滤器
	@Bean // (name = SHIRO_FILTER)
	public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {
		FilterRegistrationBean<DelegatingFilterProxy> filterReg = new FilterRegistrationBean<DelegatingFilterProxy>();
		DelegatingFilterProxy filterProxy = new DelegatingFilterProxy();
		filterProxy.setTargetFilterLifecycle(true);
		filterProxy.setTargetBeanName(SHIRO_FILTER);
		filterReg.setFilter(filterProxy);
		filterReg.addUrlPatterns("/*");
		return filterReg;
	}

	/* 加入注解的使用，不加入这个注解不生效--开始 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}
	/* 加入注解的使用，不加入这个注解不生效--结束 */

	// 这里是为了能在html页面引用shiro标签，上面两个函数必须添加，不然会报错
	@Bean(name = "shiroDialect")
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}
}
