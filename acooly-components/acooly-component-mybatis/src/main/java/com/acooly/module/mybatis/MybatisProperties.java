/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * yanglie@yiji.com 2015-09-09 09:20 创建
 *
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.Apps;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.ibatis.session.LocalCacheScope;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo
 */
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
@Data
public class MybatisProperties implements InitializingBean {
	
	public static final String PREFIX = "acooly.mybatis";
	
	/**
	 * 是否启用此组件
	 */
	private boolean enable = true;
	
	/**
	 * 可选: mybatis配置
	 * ,ref:http://www.mybatis.org/mybatis-3/configuration.html#settings
	 */
	private Map<String, String> settings;
	/**
	 * 可选：自定义类型处理器TypeHandler所在的包，多个包用逗号分隔
	 */
	private String typeHandlersPackage;
	
	private String typeAliasesPackage = Apps.getBasePackage() + ".domain";
	private String[] mapperLocations;
	private String configLocation;
	@Override
	public void afterPropertiesSet() throws Exception {
		if (settings == null) {
			settings = Maps.newHashMap();
		}
		if (!settings.containsKey("localCacheScope")) {
			settings.put("localCacheScope", LocalCacheScope.STATEMENT.name());
		}
		if (typeHandlersPackage == null) {
			typeHandlersPackage = "com.acooly.module.mybatis.typehandler";
		} else {
			typeHandlersPackage += ",com.acooly.module.mybatis.typehandler";
		}
	}
	
	public Resource[] resolveMapperLocations() {
		List<Resource> resources = new ArrayList<Resource>();
		if (this.mapperLocations != null) {
			for (String mapperLocation : this.mapperLocations) {
				Resource[] mappers;
				try {
					mappers = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
					resources.addAll(Arrays.asList(mappers));
				} catch (IOException e) {
					
				}
			}
		}
		
		Resource[] mapperLocations = new Resource[resources.size()];
		mapperLocations = resources.toArray(mapperLocations);
		return mapperLocations;
	}
	
}
