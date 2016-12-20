/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-06-16 17:52 创建
 *
 */
package com.acooly.core.common.boot.component.web;

import com.acooly.core.common.boot.EnvironmentHolder;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import freemarker.template.utility.XmlEscape;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableWebMvc
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class })
@EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class, WebProperties.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Slf4j
public class WebAutoConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware, InitializingBean {
	
	public static final String SIMPLE_URL_MAPPING_VIEW_CONTROLLER = "simpleUrlMappingViewController";
	
	@Autowired
	private WebProperties webProperties;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//设置 welcome-file
		if (!Strings.isNullOrEmpty(webProperties.getWelcomeFile())) {
			String welcomeFile = webProperties.getWelcomeFile();
			if (!welcomeFile.startsWith("/")) {
				welcomeFile = "forward:/" + welcomeFile;
			} else {
				welcomeFile = "forward:" + welcomeFile;
			}
			registry.addViewController("/").setViewName(welcomeFile);
			registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		}
	}
	
	/**
	 * 配置模板直接映射bean
	 */
	@Bean
	public SimpleUrlHandlerMapping directUrlHandlerMapping(WebProperties webProperties) {
		SimpleUrlHandlerMapping directUrlHandlerMapping = new SimpleUrlHandlerMapping();
		directUrlHandlerMapping.setOrder(Integer.MAX_VALUE - 2);
		Map<String, Object> urlMap = new HashMap<>();
		for (String url : webProperties.buildMappingUrlList()) {
			urlMap.put(url, SIMPLE_URL_MAPPING_VIEW_CONTROLLER);
		}
		directUrlHandlerMapping.setUrlMap(urlMap);
		return directUrlHandlerMapping;
	}
	
	/**
	 * 配置模板直接映射controller
	 */
	@Bean(name = SIMPLE_URL_MAPPING_VIEW_CONTROLLER)
	public SimpleUrlMappingViewController simpleUrlMappingViewController(WebProperties webProperties) {
		SimpleUrlMappingViewController simpleUrlMappingViewController = new SimpleUrlMappingViewController();
		Map<String, String> viewNameMap = webProperties.buildViewNameMap();
		if (!viewNameMap.isEmpty()) {
			log.info("配置url直接映射模板:{}", viewNameMap);
		}
		simpleUrlMappingViewController.setViewNameMap(viewNameMap);
		return simpleUrlMappingViewController;
	}
	
	@Bean
	@ConditionalOnBean(HiddenHttpMethodFilter.class)
	public FilterRegistrationBean disableHiddenHttpMethodFilter(HiddenHttpMethodFilter filter,
																WebProperties webProperties) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(webProperties.isHiddenHttpMethodFilterEnable());
		return registration;
	}
	
	@Bean
	@ConditionalOnBean(HttpPutFormContentFilter.class)
	public FilterRegistrationBean disableHttpPutFormContentFilter(HttpPutFormContentFilter filter,
																	WebProperties webProperties) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(webProperties.isHttpPutFormContentFilterEnable());
		return registration;
	}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML).favorPathExtension(true)
			.favorParameter(false).useJaf(false).mediaType("html", MediaType.APPLICATION_JSON)
			.mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON);
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		super.configureViewResolvers(registry);
		Map<String, Object> attributes= Maps.newHashMap();
		attributes.put("requestContextAttribute","rc");
		attributes.put("xml_escape",new XmlEscape());
		registry.freeMarker().attributes(attributes);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, WebMvcAutoConfiguration> beansOfType = applicationContext
			.getBeansOfType(WebMvcAutoConfiguration.class);
		if (beansOfType.isEmpty()) {
			log.error("yiji-boot spring mvc 没有正确加载WebMvcAutoConfiguration,原因可能有:");
			log.error("1. JavaConfig中配置了@EnableWebMvc");
			log.error("2. 引入了spring-mvc xml配置文件");
		}
		if (EnvironmentHolder.get().getProperty(WebProperties.Jsp.ENABLE_KEY, Boolean.class, Boolean.TRUE)) {
			try {
				InternalResourceViewResolver internalResourceViewResolver = applicationContext
					.getBean(InternalResourceViewResolver.class);
				internalResourceViewResolver.setPrefix(webProperties.getJsp().getPrefix());
				internalResourceViewResolver.setSuffix(webProperties.getJsp().getSuffix());
			} catch (BeansException e) {
				// do nothing
			}
		}
		
	}
}
