/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-25 14:00 创建
 */
package com.acooly.module.jpa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.module.jpa.JPAProperties.PREFIX;


/**
 * @author qiubo
 */
@ConfigurationProperties(prefix = PREFIX)
@Getter
@Setter
public class JPAProperties {
	public static final String PREFIX = "acooly.jpa";
	public static final String ENABLE_KEY = PREFIX + ".enable";
	private boolean enable = true;
	private boolean openEntityManagerInViewFilterEnable=true;
	
}
