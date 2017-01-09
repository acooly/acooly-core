/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-09-10 18:47 创建
 *
 */
package com.acooly.module.tomcat;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo
 */
public class TomcatComponentInitializer implements ComponentInitializer {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		//enable org.apache.catalina.valves.RemoteIpValve for get real ip （ accesslog and request.getRemoteAddr() all will get real ip ）
		System.setProperty("server.tomcat.remote_ip_header", "x-forwarded-for");
		//enable https redirct to relative location
		System.setProperty("server.tomcat.protocol_header", "x-forwarded-proto");
		System.setProperty("server.tomcat.port-header", "x-forwarded-port");
	}
}
