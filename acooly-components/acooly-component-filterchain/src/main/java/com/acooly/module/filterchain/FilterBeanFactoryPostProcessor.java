/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-02-19 00:43 创建
 */
package com.acooly.module.filterchain;

import com.google.common.base.Splitter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * 扫描指定包中的过滤器
 * @author qiubo@yiji.com
 */
public class FilterBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {
	
	private String scanPackage;
	
	public FilterBeanFactoryPostProcessor(String scanPackage) {
		this.scanPackage = scanPackage;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}
	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (this.scanPackage != null) {
			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
			scanner.addIncludeFilter(new AssignableTypeFilter(Filter.class));
			scanner.addIncludeFilter(new AssignableTypeFilter(FilterChainBase.class));
			scanner.scan(Splitter.on(",").splitToList(scanPackage).toArray(new String[0]));
		}
	}
}
