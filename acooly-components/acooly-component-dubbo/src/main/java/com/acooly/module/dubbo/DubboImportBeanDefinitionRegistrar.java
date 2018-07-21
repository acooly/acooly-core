package com.acooly.module.dubbo;

import com.acooly.core.common.boot.Apps;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.ServiceAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-20 18:33
 */
public class DubboImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Set<String> packagesToScan = Apps.buildProperties(DubboProperties.class).getPackagesToScan();

        registerServiceAnnotationBeanPostProcessor(packagesToScan, registry);

        registerReferenceAnnotationBeanPostProcessor(registry);

    }

    private void registerServiceAnnotationBeanPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = rootBeanDefinition(ServiceAnnotationBeanPostProcessor.class);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }


    private void registerReferenceAnnotationBeanPostProcessor(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(ReferenceAnnotationBeanPostProcessor.BEAN_NAME)) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition(ReferenceAnnotationBeanPostProcessor.class);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(ReferenceAnnotationBeanPostProcessor.BEAN_NAME, beanDefinition);
        }
    }
}
