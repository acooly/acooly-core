/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-10-27 23:31 创建
 */
package com.acooly.module.security.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(SecurityProperties.PREFIX)
@Data
public class SecurityProperties {
	
	public static final String PREFIX = "acooly.security";
	/**
	 * 是否启用shiro
	 */
	private boolean enable = true;
	
	private Shiro shiro = new Shiro();
	private CSRF csrf = new CSRF();
	private Xss xss = new Xss();
	private Captcha captcha = new Captcha();
	
	@Getter
	@Setter
	public static class Shiro {
		
		/**
		 * 是否启用shiro
		 */
		private boolean enable = true;
		
		/**
		 * 登录页面链接
		 */
		private String loginUrl = "/manage/login.html";
		
		/**
		 * 没有权限时跳转的链接
		 */
		private String unauthorizedUrl = "/unauthorized.html";
		
		/**
		 * 登录成功后的链接
		 */
		private String successUrl = "/manage/onLoginSuccess.html";
		/**
		 * 对应shiro.ini中的[urls]标签，注意顺序，格式如：
		 * <p>
		 * <pre>
		 * acooly.shiro.urls[0]./shiro/** = authc
		 * acooly.shiro.urls[1]./** = anon
		 * </pre>
		 */
		private List<Map<String, String>> urls = Lists.newLinkedList();
		
		/**
		 * 自定义Filter列表，对应shiro.ini中的[filters]标签，格式如：
		 * <p>
		 * <pre>
		 *  yiji.shiro.filters.authc=com.yiji.neverstopfront.web.shiro.CaptchaFormAuthenticationFilter
		 *  yiji.shiro.filters.admin=com.yiji.neverstopfront.web.shiro.AdminAuthorizationFilter
		 *  houseProperty=com.yiji.neverstopfront.web.shiro.ServiceTypeAuthorizationFilter
		 *  houseProperty.serviceType=HOUSE_PROPERTY
		 *  installment=com.yiji.neverstopfront.web.shiro.ServiceTypeAuthorizationFilter
		 *  installment.serviceType=INSTALLMENT
		 *  </pre>
		 * <p>
		 * </ul>
		 */
		private LinkedHashMap<String, String> filters = Maps.newLinkedHashMap();
		
		public Shiro() {
			//添加默认url过滤器
			addUrlFilter("/manage/index.html", "authc");
			addUrlFilter("/manage/login.html", "authc");
			addUrlFilter("/manage/logout.html", "logout");
			addUrlFilter("/manage/error/**", "anon");
			addUrlFilter("/manage/assert/**", "anon");
			addUrlFilter("/manage/*.html", "anon");
			addUrlFilter("/manage/*.jsp", "user");
			addUrlFilter("/manage/layout/*", "user");
			addUrlFilter("/manage/system/*", "user");
			addUrlFilter("/manage/**", "urlAuthr");
			addUrlFilter("/**", "anon");
		}
		
		public void addUrlFilter(String key, String value) {
			Map<String, String> url = Maps.newHashMap();
			url.put(key, value);
			urls.add(url);
		}
	}
	
	@Getter
	@Setter
	public static class CSRF {
		private boolean enable = true;
		private List<String> exclusions = Lists.newArrayList();
		
		public CSRF() {
			exclusions.add("/gateway.html");
			exclusions.add("/ofile/upload.html");
		}
	}
	
	@Data
	public static class Xss {
		private boolean enable = true;
	}
	
	@Data
	public static class Captcha {
		private boolean enable = true;
		private String url = "/jcaptcha.jpg";
	}
	
}
