package com.acooly.core.common.web.support;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.core.env.AbstractEnvironment;

/**
 * Web启动时，加载所有的web.xml中定义的属性到系统变量中。
 * 
 * 已迁移到:com.acooly.core.common.web.integrator
 * 
 * @author zhangpu
 * 
 */
@Deprecated
public class SystemPropertyIntergrationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String springprofilesActive = event.getServletContext().getInitParameter(
				AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, springprofilesActive);
		event.getServletContext().log(
				"Sett System Property: '" + AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME + "' = "
						+ springprofilesActive + " according to web.xml.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

}
