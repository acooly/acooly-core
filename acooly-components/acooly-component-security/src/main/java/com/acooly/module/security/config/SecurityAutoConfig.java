/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-10-27 23:33 创建
 */
package com.acooly.module.security.config;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.module.jpa.JPAAutoConfig;
import com.acooly.module.security.captche.CaptchaServlet;
import com.acooly.module.security.defence.XssDefenseFilter;
import com.acooly.module.security.defence.csrf.CookieCsrfTokenRepository;
import com.acooly.module.security.defence.csrf.CsrfAccessDeniedHandlerImpl;
import com.acooly.module.security.defence.csrf.CsrfFilter;
import com.acooly.module.security.defence.csrf.RequireCsrfProtectionMatcher;
import com.acooly.module.security.shiro.cache.ShiroCacheManager;
import com.acooly.module.security.shiro.filter.CaptchaFormAuthenticationFilter;
import com.acooly.module.security.shiro.filter.NotifyLogoutFilter;
import com.acooly.module.security.shiro.filter.UrlResourceAuthorizationFilter;
import com.acooly.module.security.shiro.listener.ShireLoginLogoutSubject;
import com.acooly.module.security.shiro.realm.PathMatchPermissionResolver;
import com.acooly.module.security.shiro.realm.ShiroDbRealm;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.config.ReflectionBuilder;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ SecurityProperties.class, FrameworkProperties.class })
@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = { "com.acooly.module.security" })
public class SecurityAutoConfig {
	
	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".shiro.enable", matchIfMissing = true)
	@AutoConfigureAfter({ JPAAutoConfig.class, DataSourceTransactionManagerAutoConfiguration.class })
	public static class ShiroAutoConfigration {
		@Bean
		public CacheManager shiroCacheManager(RedisTemplate redisTemplate) {
			ShiroCacheManager shiroCacheManager = new ShiroCacheManager();
			shiroCacheManager.setRedisTemplate(redisTemplate);
			return shiroCacheManager;
		}
		
		@Bean
		public WebSecurityManager shiroSecurityManager(CacheManager shiroCacheManager, Realm shiroRealm) {
			
			DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
			securityManager.setCacheManager(shiroCacheManager);
			securityManager.setRealm(shiroRealm);
			return securityManager;
		}
		
		@Bean
		public Realm shiroRealm(PathMatchPermissionResolver pathMatchPermissionResolver) {
			ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
			shiroDbRealm.setPermissionResolver(pathMatchPermissionResolver);
			shiroDbRealm.setAuthenticationCachingEnabled(false);
			shiroDbRealm.setAuthorizationCachingEnabled(false);
			shiroDbRealm.setAuthorizationCacheName(ShiroCacheManager.KEY_AUTHZ);
			shiroDbRealm.setAuthenticationCacheName(ShiroCacheManager.KEY_AUTHC);
			return shiroDbRealm;
		}
		
		@Bean
		public PathMatchPermissionResolver pathMatchPermissionResolver() {
			return new PathMatchPermissionResolver();
		}
		
		@Bean
		public ShiroFilterFactoryBean shiroFilterFactoryBean(	@Qualifier("shiroSecurityManager") WebSecurityManager shiroSecurityManager,
																SecurityProperties securityProperties) {
			ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
			shiroFilter.setSecurityManager(shiroSecurityManager);
			shiroFilter.setLoginUrl(securityProperties.getShiro().getLoginUrl());
			//		shiroFilter.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
			shiroFilter.setSuccessUrl(securityProperties.getShiro().getSuccessUrl());
			shiroFilter.setFilters(buildFiltersMap(securityProperties));
			shiroFilter.setFilterChainDefinitions(getFilterChainDefinitions(securityProperties));
			
			return shiroFilter;
		}
		
		@Bean
		@DependsOn({ "logout", "urlAuthr", "authc" })
		public Filter shiroFilter(ShiroFilterFactoryBean shiroFilterFactoryBean, Realm shiroRealm) throws Exception {
			((DefaultWebSecurityManager) shiroFilterFactoryBean.getSecurityManager()).setRealm(shiroRealm);
			return (Filter) shiroFilterFactoryBean.getObject();
		}
		
		@Bean
		public FilterRegistrationBean shiroFilterRegistrationBean(	@Qualifier("shiroFilter") Filter shiroFilter,
																	SecurityProperties securityProperties) {
			FilterRegistrationBean registration = new FilterRegistrationBean();
			registration.setFilter(shiroFilter);
			registration.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
			registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp", "*.json").toArray(new String[0]));
			registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
			registration.setName("shiroFilter");
			return registration;
		}
		
		@Bean
		@ConditionalOnMissingBean(LifecycleBeanPostProcessor.class)
		public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
			return new LifecycleBeanPostProcessor();
		}
		
		@Bean
		public AuthorizationAttributeSourceAdvisor shiroAuthorizationAttributeSourceAdvisor(WebSecurityManager shiroSecurityManager) {
			AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
			advisor.setSecurityManager(shiroSecurityManager);
			return advisor;
		}
		
		@Bean
		public AuthorizationAttributeSourceAdvisor shiroAuthorizationAttributeSourceAdvisor(WebSecurityManager shiroSecurityManager,
																							Realm shiroRealm) {
			AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
			advisor.setSecurityManager(shiroSecurityManager);
			return advisor;
		}
		
		/**
		 * 注册spring bean，为了shiro能够找到filter
		 * @param shireLoginLogoutSubject
		 * @return
		 */
		@Bean
		public NotifyLogoutFilter logout(	ShireLoginLogoutSubject shireLoginLogoutSubject,
											SecurityProperties securityProperties) {
			NotifyLogoutFilter notifyLogoutFilter = new NotifyLogoutFilter();
			notifyLogoutFilter.setRedirectUrl(securityProperties.getShiro().getLoginUrl());
			notifyLogoutFilter.setShireLoginLogoutSubject(shireLoginLogoutSubject);
			return notifyLogoutFilter;
		}
		
		/**
		 * 禁用logout filter，防止spring设置为web容器filter。因为shiroFilter会代理logout
		 * filter的执行。
		 * @param filter
		 * @return
		 */
		@Bean
		public FilterRegistrationBean disableLogoutForSpringMVC(NotifyLogoutFilter filter) {
			FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
			filterRegistrationBean.setFilter(filter);
			filterRegistrationBean.setEnabled(false);
			filterRegistrationBean.setName("disableLogoutForSpringMVC");
			return filterRegistrationBean;
		}
		
		@Bean
		public UrlResourceAuthorizationFilter urlAuthr() {
			UrlResourceAuthorizationFilter filter = new UrlResourceAuthorizationFilter();
			return filter;
		}
		
		@Bean
		public FilterRegistrationBean disableUrlAuthrForSpringMVC(UrlResourceAuthorizationFilter filter) {
			FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
			filterRegistrationBean.setFilter(filter);
			filterRegistrationBean.setEnabled(false);
			filterRegistrationBean.setName("disableUrlAuthrForSpringMVC");
			return filterRegistrationBean;
		}
		
		@Bean
		public CaptchaFormAuthenticationFilter authc(	ShireLoginLogoutSubject shireLoginLogoutSubject,
														SecurityProperties securityProperties) {
			CaptchaFormAuthenticationFilter filter = new CaptchaFormAuthenticationFilter();
			filter.setShireLoginLogoutSubject(shireLoginLogoutSubject);
			filter.setFailureUrl(securityProperties.getShiro().getLoginUrl());
			filter.setSuccessUrl(securityProperties.getShiro().getSuccessUrl());
			return filter;
		}
		
		@Bean
		public FilterRegistrationBean disableAuthcForSpringMVC(CaptchaFormAuthenticationFilter filter) {
			FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
			filterRegistrationBean.setFilter(filter);
			filterRegistrationBean.setEnabled(false);
			filterRegistrationBean.setName("disableAuthcForSpringMVC");
			return filterRegistrationBean;
		}
		
		@Bean
		public ShireLoginLogoutSubject shireLoginLogoutSubject() {
			ShireLoginLogoutSubject logoutSubject = new ShireLoginLogoutSubject();
			return logoutSubject;
		}
		
		private String getFilterChainDefinitions(SecurityProperties securityProperties) {
			List<Map<String, String>> urls = securityProperties.getShiro().getUrls();
			if (CollectionUtils.isEmpty(urls)) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			for (Map<String, String> url : urls) {
				if (MapUtils.isNotEmpty(url)) {
					for (Map.Entry<String, String> entry : url.entrySet()) {
						sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
					}
				}
			}
			return sb.toString();
		}
		
		private Map<String, Filter> buildFiltersMap(SecurityProperties securityProperties) {
			Map<String, String> filters = securityProperties.getShiro().getFilters();
			if (MapUtils.isEmpty(filters)) {
				return Maps.newLinkedHashMap();
			}
			
			ReflectionBuilder builder = new ReflectionBuilder();
			Map<String, ?> built = builder.buildObjects(filters);
			return extractFilters(built);
		}
		
		private Map<String, Filter> extractFilters(Map<String, ?> objects) {
			if (CollectionUtils.isEmpty(objects)) {
				return Maps.newLinkedHashMap();
			}
			Map<String, Filter> filterMap = Maps.newLinkedHashMap();
			for (Map.Entry<String, ?> entry : objects.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof Filter) {
					filterMap.put(key, (Filter) value);
				}
			}
			return filterMap;
		}
	}
	
	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".captcha.enable", matchIfMissing = true)
	public static class CaptchaAutoConfigration {
		@Bean
		public ServletRegistrationBean jcaptchaServlet(SecurityProperties securityProperties) {
			ServletRegistrationBean bean = new ServletRegistrationBean();
			bean.setUrlMappings(Lists.newArrayList(securityProperties.getCaptcha().getUrl()));
			CaptchaServlet captchaServlet = new CaptchaServlet();
			bean.setServlet(captchaServlet);
			return bean;
		}
	}
	
	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".csrf.enable", matchIfMissing = true)
	public static class CSRFAutoConfigration {
		@Bean
		public Filter csrfFilter(SecurityProperties securityProperties) {
			CookieCsrfTokenRepository tokenRepository = new CookieCsrfTokenRepository();
			CsrfFilter csrfFilter = new CsrfFilter(tokenRepository);
			List<String> excludes = Lists.newArrayList();
			for (List<String> list : securityProperties.getCsrf().getExclusions().values()) {
				excludes.addAll(list);
			}
			csrfFilter.setRequireCsrfProtectionMatcher(new RequireCsrfProtectionMatcher(excludes));
			CsrfAccessDeniedHandlerImpl csrfAccessDeniedHandler = new CsrfAccessDeniedHandlerImpl();
			csrfAccessDeniedHandler.setErrorPage(securityProperties.getCsrf().getErrorPage());
			csrfFilter.setAccessDeniedHandler(csrfAccessDeniedHandler);
			FilterRegistrationBean registration = new FilterRegistrationBean();
			registration.setFilter(csrfFilter);
			registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp").toArray(new String[0]));
			registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
			registration.setName("csrfDefenseFilter");
			return csrfFilter;
		}
		
	}
	
	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".xss.enable", matchIfMissing = true)
	public static class XssAutoConfigration {
		@Bean
		public FilterRegistrationBean xssFilter(SecurityProperties securityProperties) {
			XssDefenseFilter filter = new XssDefenseFilter();
			
			FilterRegistrationBean registration = new FilterRegistrationBean();
			registration.setFilter(filter);
			registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp").toArray(new String[0]));
			registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
			registration.setName("xssDefenseFilter");
			return registration;
		}
		
	}
	
	@Bean
	public StandardDatabaseScriptIniter securityScriptIniter() {
		return new StandardDatabaseScriptIniter() {
			
			@Override
			public String getEvaluateTable() {
				return "SYS_USER";
			}
			
			@Override
			public String getComponentName() {
				return "security";
			}
			
			@Override
			public List<String> getInitSqlFile() {
				return Lists.newArrayList("security");
			}
		};
	}
}
