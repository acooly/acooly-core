/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-25 16:19 创建
 *
 */
package com.acooly.core.common.boot.component.tomcat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties("yiji.tomcat")
@Data
public class TomcatProperties {
	public static final String HTTP_ACCESS_LOG_FORMAT = "%h %l [%{yyyy-MM-dd HH:mm:ss.SSS}t] \"%r\" %s %b %D";
	/**
	 * 可选：最小空闲线程
	 */
	private int minSpareThreads = 20;
	/**
	 * 可选：最大线程数
	 */
	private volatile int maxThreads = 400;
	
	/**
	 * 可选：是否启用访问日志
	 */
	private boolean accessLogEnable = false;
	
	/**
	 * 可选: 设置uri编码
	 */
	private String uriEncoding = "UTF-8";
	
	private Jsp jsp = new Jsp();
	
	@Data
	public static class Jsp {
		
		public static final String ENABLE_KEY = "yiji.tomcat.jsp.enable";
		/**
		 * 是否启用jsp
		 */
		private boolean enable = true;
		/**
		 * jsp文件路径前缀
		 */
		private String prefix = "/WEB-INF/jsp/";
		/**
		 * jsp扩展名
		 */
		private String suffix = ".jsp";
		
	}
	
}
