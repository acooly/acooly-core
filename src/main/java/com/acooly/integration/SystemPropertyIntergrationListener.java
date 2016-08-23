package com.acooly.integration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

/**
 * Web启动时，加载所有的web.xml中定义的特定<context-param>到系统变量System.getProperty中。
 * 
 * 目前处理：1、spring.profiles.active ,2.webapp.root
 * 
 * @author zhangpu
 * 
 */
public class SystemPropertyIntergrationListener implements ServletContextListener {
	
	private static final String SPRING_PROFILE_ACTIVE_KEY_PARAM = "spring.profiles.active";
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		setSpringProfileActiveSystemProperty(servletContext);
		WebUtils.setWebAppRootSystemProperty(servletContext);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		removeSpringProfileActiveSystemProperty();
		WebUtils.removeWebAppRootSystemProperty(event.getServletContext());
	}
	
	private void setSpringProfileActiveSystemProperty(ServletContext servletContext) {
		String profile = System.getProperty(SPRING_PROFILE_ACTIVE_KEY_PARAM);
		if (StringUtils.isNotBlank(profile)) {
			servletContext.log("Use System property " + SPRING_PROFILE_ACTIVE_KEY_PARAM + " = "
								+ profile);
			return;
		}
		
		profile = servletContext.getInitParameter(SPRING_PROFILE_ACTIVE_KEY_PARAM);
		if (StringUtils.isBlank(profile)) {
			servletContext.log("env setting[" + SPRING_PROFILE_ACTIVE_KEY_PARAM
								+ "] does not exist form system-property and web-context-param");
			return;
		}
		System.setProperty(SPRING_PROFILE_ACTIVE_KEY_PARAM, profile);
		servletContext
			.log("Use context-param " + SPRING_PROFILE_ACTIVE_KEY_PARAM + " = " + profile);
	}
	
	private void removeSpringProfileActiveSystemProperty() {
		System.clearProperty(SPRING_PROFILE_ACTIVE_KEY_PARAM);
	}
}
