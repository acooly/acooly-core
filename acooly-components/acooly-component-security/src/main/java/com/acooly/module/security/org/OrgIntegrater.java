package com.acooly.module.security.org;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OrgIntegrater implements ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private OrgIntegrationService orgIntegrationService;

	public OrgIntegrationService getOrgIntegrationService() {
		return orgIntegrationService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}
