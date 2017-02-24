/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-05 15:30 创建
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.Apps;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import static com.acooly.core.common.boot.listener.ExApplicationRunListener.COMPONENTS_PACKAGE;

/**
 * @author qiubo@yiji.com
 */
public class MapperScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
	
	private BeanFactory beanFactory;
	
	private ResourceLoader resourceLoader;
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		try {
			if (this.resourceLoader != null) {
				scanner.setResourceLoader(this.resourceLoader);
			}
			scanner.setMarkerInterface(EntityMybatisDao.class);
			scanner.registerFilters();
			scanner.doScan(Apps.getBasePackage(),COMPONENTS_PACKAGE+".**.dao");
		} catch (IllegalStateException ex) {
		}
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}