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

import java.util.Collection;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class MapperScannerRegistrar
        implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private BeanFactory beanFactory;

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        MybatisProperties mybatisProperties = Apps.buildProperties(MybatisProperties.class);
        if (!mybatisProperties.isSupportMultiDataSource()) {
            ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
            try {
                if (this.resourceLoader != null) {
                    scanner.setResourceLoader(this.resourceLoader);
                }
                scanner.setMarkerInterface(EntityMybatisDao.class);
                scanner.registerFilters();
                Collection<String> daoScanPackages = mybatisProperties.getDaoScanPackages().values();
                scanner.doScan(daoScanPackages.toArray(new String[daoScanPackages.size()]));
            } catch (IllegalStateException ex) {
            }
        } else {
            for (Map.Entry<String, MybatisProperties.Multi> entry :
                    mybatisProperties.getMulti().entrySet()) {
                ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
                try {
                    if (this.resourceLoader != null) {
                        scanner.setResourceLoader(this.resourceLoader);
                    }
                    scanner.setMarkerInterface(EntityMybatisDao.class);
                    if (entry.getValue().isPrimary()) {
                        scanner.setSqlSessionFactoryBeanName("sqlSessionFactory");
                    } else {
                        scanner.setSqlSessionFactoryBeanName(entry.getKey() + "SqlSessionFactory");
                    }
                    scanner.registerFilters();
                    scanner.doScan(entry.getValue().getScanPackage());
                } catch (IllegalStateException ex) {
                }
            }
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
