/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-10-19 17:35 创建
 *
 */
package com.acooly.module.web;

import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanglie@yiji.com
 */
@ConfigurationProperties("acooly.web")
@Data
public class WebProperties {
	/**
	 * 配置直接返回静态模板页面的映射 /index.htm->index,/test/test.htm->test/detail
	 */
	private Map<String,String> simplePageMap;
	
	/**
	 * 是否启用hiddenHttpMethodFilter
	 * @see WebMvcAutoConfiguration#hiddenHttpMethodFilter()
	 */
	private boolean hiddenHttpMethodFilterEnable = false;
	
	/**
	 * 是否启用httpPutFormContentFilter
	 * @see WebMvcAutoConfiguration#httpPutFormContentFilter()
	 */
	private boolean HttpPutFormContentFilterEnable = false;
	
	/**
	 * 配置自定义欢迎页面,比如设置登录页面为欢迎页:login.html
	 */
	private String welcomeFile = "index.html";
	
	/**
	 * http 缓存时间,-1=不设置,0=第二次请求需要和服务器协商,大于0=经过多少秒后才过期
	 */
	private int cacheMaxAge = -1;

	private Jsp jsp = new Jsp();
	
	public List<String> buildMappingUrlList() {
		List mappingUrlList = new ArrayList<>();
		if (simplePageMap == null) {
			return mappingUrlList;
		}
		String simplePageMaps="";
        for (String s : simplePageMap.values()) {
            simplePageMaps+=s;
        }
        String[] settingEntries = simplePageMaps.split(",");
		for (String settingEntry : settingEntries) {
			String[] singleMapping = settingEntry.split("->");
			if (singleMapping == null || singleMapping.length != 2) {
				throw new RuntimeException("直接返回模板页面的配置格式不正确");
			}
			String key = singleMapping[0].trim();
			if (!key.startsWith("/")) {
				key = "/" + key;
			}
			mappingUrlList.add(key);
		}
		return mappingUrlList;
	}
	
	public Map<String, String> buildViewNameMap() {
		Map<String, String> viewNameMap = new HashMap<>();
		if (simplePageMap == null) {
			return viewNameMap;
		}
        String simplePageMaps="";
        for (String s : simplePageMap.values()) {
            simplePageMaps+=s;
        }
		String[] settingEntries = simplePageMaps.split(",");
		for (String settingEntry : settingEntries) {
			String[] singleMapping = settingEntry.split("->");
			if (singleMapping == null || singleMapping.length != 2) {
				throw new RuntimeException("直接返回模板页面的配置格式不正确");
			}
			String key = singleMapping[0].trim();
			if (!key.startsWith("/")) {
				key = "/" + key;
			}
			viewNameMap.put(key, singleMapping[1].trim());
		}
		return viewNameMap;
	}
	
	@Data
	public static class Jsp {
		
		public static final String ENABLE_KEY = "acooly.web.jsp.enable";
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
