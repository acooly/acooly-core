package com.acooly.module.test;

import com.acooly.core.common.boot.Apps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/** @author qiubo@yiji.com */
public class LazyInitBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  private Class<?>[] exclusionList;

  private Boolean isTest;

  public LazyInitBeanFactoryPostProcessor() {}

  public LazyInitBeanFactoryPostProcessor(Class<?>[] exclusionList) {
    this.exclusionList = exclusionList;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    if (isTest == null) {
      ConfigurableEnvironment environment = (ConfigurableEnvironment) Apps.getEnvironment();
      MutablePropertySources propertySources = environment.getPropertySources();
      isTest = propertySources.contains("Inlined Test Properties");
    }
    if (!isTest) {
      return;
    }
    for (String beanName : beanFactory.getBeanDefinitionNames()) {
      if (isLazy(beanName, beanFactory)) {
        BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
        String beanClassName = definition.getBeanClassName();
        if (beanClassName != null && beanClassName.contains("com.acooly.module.mybatis")) {
          continue;
        }
        definition.setLazyInit(true);
      }
    }
  }

  private boolean isLazy(String beanName, ConfigurableListableBeanFactory beanFactory) {
    if (exclusionList == null || exclusionList.length == 0) {
      return true;
    }
    for (Class<?> clazz : exclusionList) {
      if (beanFactory.isTypeMatch(beanName, clazz)) {
        return false;
      }
    }
    return true;
  }
}
