/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-07 15:23 创建
 */
package com.acooly.core.common.boot.listener;

import com.acooly.core.common.boot.Apps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class DevModeDetector {
	private static final Map<String, Object> PROPERTIES;
	private static volatile boolean inited = false;
	static {
		HashMap properties = new HashMap();
		properties.put("spring.thymeleaf.cache", "false");
		properties.put("spring.freemarker.cache", "false");
		properties.put("spring.groovy.template.cache", "false");
		properties.put("spring.velocity.cache", "false");
		properties.put("spring.mustache.cache", "false");
		properties.put("server.session.persistent", "true");
		properties.put("spring.h2.console.enabled", "true");
		properties.put("spring.resources.cache-period", "0");
		properties.put("spring.resources.chain.cache", "false");
		properties.put("spring.template.provider.cache", "false");
		properties.put("spring.mvc.log-resolved-exception", "true");
		PROPERTIES = Collections.unmodifiableMap(properties);
	}
	
	public void apply(ConfigurableEnvironment environment) {
		if (inited) {
			return;
		}
		inited = true;
		if (environment.getPropertySources().contains("refresh")) {
			System.setProperty(Apps.DEV_MODE_KEY, Boolean.TRUE.toString());
		} else if (runInIDE()) {
			MapPropertySource propertySource = new MapPropertySource("refresh", PROPERTIES);
			environment.getPropertySources().addLast(propertySource);
		}
	}
	
	private boolean runInIDE() {
		try {
			ProtectionDomain protectionDomain = getClass().getProtectionDomain();
			CodeSource codeSource = protectionDomain.getCodeSource();
			URI location = (codeSource == null ? null : codeSource.getLocation().toURI());
			String path = (location == null ? null : location.getSchemeSpecificPart());
			if (path != null) {
				boolean isRunInIDE = !path.contains("!");
				System.setProperty(Apps.DEV_MODE_KEY, Boolean.valueOf(isRunInIDE).toString());
				return isRunInIDE;
			}
		} catch (Exception e) {
			log.warn("DevToolsDetector failure:{}", e.getMessage());
		}
		return false;
	}
}
