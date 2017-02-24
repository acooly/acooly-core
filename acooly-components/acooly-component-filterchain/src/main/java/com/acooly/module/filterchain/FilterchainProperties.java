/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 *
 * 修订记录:
 * zhouxi@yiji.com 2015-09-15 15:41 创建
 *
 */
package com.acooly.module.filterchain;

import com.acooly.core.common.boot.Apps;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.core.common.boot.listener.ExApplicationRunListener.COMPONENTS_PACKAGE;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties("acooly.filterchain")
@Data
public class FilterchainProperties {
	
	private static final String DEFAULT_SCAN_PACKAGE = Apps.getBasePackage() + ".**.filter," + COMPONENTS_PACKAGE+".**.filter";
	/**
	 * 是否启用组件
	 */
	private boolean enable = true;
	
	/**
	 * filter扫描包,多个包之间用逗号隔开,默认使用应用base package
	 */
	private String scanPackage;

	
	public String getScanPackage() {
		if (Strings.isNullOrEmpty(scanPackage)) {
			return DEFAULT_SCAN_PACKAGE;
		} else {
			return DEFAULT_SCAN_PACKAGE + "," + scanPackage.trim();
		}
	}
}
