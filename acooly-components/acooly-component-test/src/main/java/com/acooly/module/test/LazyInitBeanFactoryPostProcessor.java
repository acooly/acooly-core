package com.acooly.module.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author qiubo@yiji.com
 */
public class LazyInitBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private Class<?>[] exclusionList;

    public LazyInitBeanFactoryPostProcessor() {
    }

    public LazyInitBeanFactoryPostProcessor(Class<?>[] exclusionList) {
        this.exclusionList = exclusionList;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        //Iterate over all bean, mark them as lazy if they are not in the exclusion list.
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            if (isLazy(beanName, beanFactory)) {
                BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
                definition.setLazyInit(true);
            }
        }
    }

    private boolean isLazy(String beanName, ConfigurableListableBeanFactory beanFactory) {
        if (exclusionList == null || exclusionList.length == 0) {
            return true;
        }
        for (Class<?> clazz : exclusionList) {
            if (beanFactory.isTypeMatch(beanName,clazz)) {
                return false;
            }
        }
        return true;
    }
}
