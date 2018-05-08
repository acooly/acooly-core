/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-05-08 11:31 创建
 */
package com.acooly.module.dubbo.mock;

import com.acooly.core.common.boot.Apps;
import com.acooly.module.dubbo.ConsumerLogFilter;
import com.acooly.module.dubbo.ProviderLogFilter;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import static com.acooly.module.dubbo.ProviderLogFilter.formatMethodGroup;

/**
 * @author shuijing
 */
@Slf4j
public class LogFilterIgnoreBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (!isMatchPackage(targetClass)) {
            return bean;
        }
        Service service = targetClass.getAnnotation(Service.class);
        if (service != null) {
            ReflectionUtils.doWithMethods(
                    bean.getClass(),
                    method -> {
                        LogFilterIgnoreMethod ignoreMethod = method.getAnnotation(LogFilterIgnoreMethod.class);
                        if (ignoreMethod != null) {
                            Class[] intfs = targetClass.getInterfaces();
                            if (intfs != null && intfs.length == 1) {
                                String interfaceName = intfs[0].getName();
                                String group = service.group();
                                String methodName = method.getName();
                                String formatMethod = formatMethodGroup(interfaceName, group, methodName);
                                log.info("dubbo忽略打印日志，method:{}", formatMethod);

                                //consumer provider都不打印
                                ConsumerLogFilter.addIgnoreLogMethod(formatMethod);
                                ProviderLogFilter.addIgnoreLogMethod(formatMethod);
                            } else {
                                throw new IllegalStateException(
                                        "服务类 "
                                                + targetClass.getName()
                                                + ",不能实现多个接口");
                            }
                        }
                    });
        }
        return bean;
    }


    private boolean isMatchPackage(Class clazz) {
        return clazz.getName().startsWith(Apps.getBasePackage());
    }
}
