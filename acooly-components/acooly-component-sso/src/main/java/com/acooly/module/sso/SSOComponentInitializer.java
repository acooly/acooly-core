package com.acooly.module.sso;

import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.boot.component.ComponentInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author shuijing
 */
@Slf4j
public class SSOComponentInitializer implements ComponentInitializer {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		String property = EnvironmentHolder.get().getProperty("acooly.sso.enable");
		if (property == null) {
			System.setProperty("acooly.security.shiro.auth.enable", "false");
		} else {
			Boolean enable = Boolean.valueOf(property);
			if (enable) {
				System.setProperty("acooly.security.shiro.auth.enable", "false");
			} else {
				System.setProperty("acooly.security.shiro.auth.enable", "true");
			}
		}
	}
}
