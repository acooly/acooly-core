/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-10-27 23:33 创建
 */
package com.acooly.module.security.config;

import com.acooly.core.common.dao.jpa.AbstractEntityJpaDao;
import com.acooly.module.security.SecurityConstants;
import com.acooly.module.security.dao.UserDao;
import com.acooly.module.security.defence.XssDefenseFilter;
import com.acooly.module.security.defence.csrf.CsrfDefenseFilter;
import com.acooly.module.security.defence.csrf.CsrfRequestMatcher;
import com.acooly.module.security.jcaptcha.AcoolyImageCaptchaEngine;
import com.acooly.module.security.jcaptcha.ImageCaptchaServlet;
import com.acooly.module.security.shiro.cache.ShiroCacheManager;
import com.acooly.module.security.shiro.filter.CaptchaFormAuthenticationFilter;
import com.acooly.module.security.shiro.filter.NotifyLogoutFilter;
import com.acooly.module.security.shiro.filter.UrlResourceAuthorizationFilter;
import com.acooly.module.security.shiro.listener.ShireLoginLogoutSubject;
import com.acooly.module.security.shiro.realm.PathMatchPermissionResolver;
import com.acooly.module.security.shiro.realm.ShiroDbRealm;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.config.ReflectionBuilder;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ SecurityProperties.class })
@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackageClasses = SecurityConstants.class)
//@EntityScan(basePackages="com.acooly.module.security.domain")
@EnableJpaRepositories(repositoryBaseClass = AbstractEntityJpaDao.class,basePackages ="com.acooly.module.security.dao")
public class SecurityAutoConfiguration {
//	@Bean
//	public JpaRepositoryFactoryBean userDao() {
//		JpaRepositoryFactoryBean factory = new JpaRepositoryFactoryBean();
//		factory.setRepositoryInterface(UserDao.class);
//		return factory;
//	}
	
	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".shiro.enable", matchIfMissing = true)
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
			//延迟加载shiroRealm。由于shiroFilterFactoryBean是FactoryBean会提前被加载。
			if (shiroRealm instanceof CachingRealm) {
				CachingRealm realm = (CachingRealm) shiroRealm;
				realm.setCachingEnabled(true);
			}
			if (shiroRealm instanceof AuthorizingRealm) {
				AuthorizingRealm realm = (AuthorizingRealm) shiroRealm;
				realm.setAuthorizationCachingEnabled(true);
				realm.setAuthorizationCacheName(ShiroCacheManager.KEY_AUTHZ);
			}
			if (shiroRealm instanceof AuthenticatingRealm) {
				AuthenticatingRealm realm = (AuthenticatingRealm) shiroRealm;
				realm.setAuthenticationCachingEnabled(true);
				realm.setAuthenticationCacheName(ShiroCacheManager.KEY_AUTHC);
			}
			customSecurityManager(shiroFilterFactoryBean.getSecurityManager(), shiroRealm);
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
		
		/**
		 * 延迟加载shiroRealm。由于shiroFilterFactoryBean是FactoryBean会提前被加载。
		 *
		 * @param shiroSecurityManager
		 * @param shiroRealm
		 */
		private void customSecurityManager(	org.apache.shiro.mgt.SecurityManager shiroSecurityManager,
											Realm shiroRealm) {
			
			DefaultWebSecurityManager securityManager = ((DefaultWebSecurityManager) shiroSecurityManager);
			if (securityManager.getRealms() == null) {
				
				if (shiroRealm instanceof CachingRealm) {
					CachingRealm realm = (CachingRealm) shiroRealm;
					realm.setCachingEnabled(true);
				}
				if (shiroRealm instanceof AuthorizingRealm) {
					AuthorizingRealm realm = (AuthorizingRealm) shiroRealm;
					realm.setAuthorizationCachingEnabled(true);
					realm.setAuthorizationCacheName(ShiroCacheManager.KEY_AUTHZ);
				}
				if (shiroRealm instanceof AuthenticatingRealm) {
					AuthenticatingRealm realm = (AuthenticatingRealm) shiroRealm;
					realm.setAuthenticationCachingEnabled(true);
					realm.setAuthenticationCacheName(ShiroCacheManager.KEY_AUTHC);
				}
				securityManager.setRealm(shiroRealm);
			}
		}
		
		@Bean
		public AuthorizationAttributeSourceAdvisor shiroAuthorizationAttributeSourceAdvisor(WebSecurityManager shiroSecurityManager,
																							Realm shiroRealm) {
			customSecurityManager(shiroSecurityManager, shiroRealm);
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
		public FastHashMapCaptchaStore fastHashMapCaptchaStore() {
			return new FastHashMapCaptchaStore();
		}
		
		@Bean
		public AcoolyImageCaptchaEngine acoolyImageCaptchaEngine() {
			return new AcoolyImageCaptchaEngine();
		}
		
		@Bean
		public ImageCaptchaService imageCaptchaService(	FastHashMapCaptchaStore fastHashMapCaptchaStore,
														AcoolyImageCaptchaEngine acoolyImageCaptchaEngine) {
			DefaultManageableImageCaptchaService captchaService = new DefaultManageableImageCaptchaService(
				fastHashMapCaptchaStore, acoolyImageCaptchaEngine, 180, 100000, 75000);
			return captchaService;
		}
		
		@Bean
		public ServletRegistrationBean jcaptchaServlet(	SecurityProperties securityProperties,
														ImageCaptchaService imageCaptchaService) {
			ServletRegistrationBean bean = new ServletRegistrationBean();
			bean.setUrlMappings(Lists.newArrayList(securityProperties.getCaptcha().getUrl()));
			ImageCaptchaServlet imageCaptchaServlet = new ImageCaptchaServlet();
			imageCaptchaServlet.setImageCaptchaService(imageCaptchaService);
			bean.setServlet(imageCaptchaServlet);
			return bean;
		}
	}
	
	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(value = SecurityProperties.PREFIX + ".csrf.enable", matchIfMissing = true)
	public static class CSRFAutoConfigration {
		@Bean
		public FilterRegistrationBean csrfFilter(SecurityProperties securityProperties) {
			CsrfDefenseFilter csrfDefenseFilter = new CsrfDefenseFilter();
			CsrfRequestMatcher csrfRequestMatcher = new CsrfRequestMatcher();
			csrfRequestMatcher.setAllowedUrls(securityProperties.getCsrf().getExclusions());
			csrfDefenseFilter.setRequireCsrfProtectionMatcher(csrfRequestMatcher);
			
			FilterRegistrationBean registration = new FilterRegistrationBean();
			registration.setFilter(csrfDefenseFilter);
			registration.addUrlPatterns(Lists.newArrayList("*.html", "*.jsp").toArray(new String[0]));
			registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
			registration.setName("csrfDefenseFilter");
			return registration;
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
}
