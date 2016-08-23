package com.acooly.integration.log;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.util.WebUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

/**
 * slft logback 日志整合器
 * 
 * 在Web容器启动时，整合JDK,apache-common和log4j日志统一使用slf4j输出。
 * 
 * @author zhangpu
 * 
 */
public class LogbackIntergrationListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(LogbackIntergrationListener.class);
	private static final String CONFIG_LOCATION = "logConfigLocation";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		// 从web.xml中加载指定文件名的日志配置文件
		String locationConfig = servletContext.getInitParameter(CONFIG_LOCATION);
		String springActiveProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		if (StringUtils.isBlank(springActiveProfile)) {
			springActiveProfile = servletContext.getInitParameter(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		}
		String location = locationConfig;
		if (StringUtils.isNotBlank(springActiveProfile)) {
			location = StringUtils.replace(locationConfig, "logback.xml", "logback." + springActiveProfile + ".xml");
		}

		try {
			if (!ResourceUtils.isUrl(location)) {
				location = SystemPropertyUtils.resolvePlaceholders(location);
				location = WebUtils.getRealPath(servletContext, location);
			}
			Resource resource = new DefaultResourceLoader().getResource(location);
			if (resource.exists()) {
				servletContext.log("Initializing slf4j from [" + location + "]");
				LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
				loggerContext.reset();
				JoranConfigurator joranConfigurator = new JoranConfigurator();
				joranConfigurator.setContext(loggerContext);
				joranConfigurator.doConfigure(resource.getInputStream());
			} else {
				servletContext.log("logConfigLocation [" + location + "] does not exist. use the default logger.");
			}
		} catch (Exception e) {
			logger.error("can loading slf4j configure file from " + location, e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}
