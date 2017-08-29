package com.acooly.module.dubbo.mock;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.exception.AppConfigException;
import com.alibaba.dubbo.config.annotation.Reference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Map;

/** @author qiubo@yiji.com */
@Slf4j
@Getter
@Setter
public class DubboMockBeanPostProcessor implements BeanPostProcessor {
  private String annotationPackage;
  private List<String> mockInterfaces;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {
    return bean;
  }

  private boolean isMatchPackage(Object bean) {
    return isMatchPackage(bean.getClass());
  }

  private boolean isMatchPackage(Class clazz) {
    if (annotationPackage == null) {
      return true;
    }
    String beanClassName = clazz.getName();
    return beanClassName.startsWith(annotationPackage);
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (mockInterfaces == null) {
      return bean;
    }
    if (!isMatchPackage(bean)) {
      return bean;
    }
    //处理代理bean，不能获取targetClass
    ReflectionUtils.doWithFields(
        bean.getClass(),
        field -> {
          Reference reference = field.getAnnotation(Reference.class);
          if (reference != null) {
            if (mockInterfaces.contains(field.getType().getName())) {
              field.setAccessible(true);
              Map<String, ?> beansOfType =
                  Apps.getApplicationContext().getBeansOfType(field.getType());
              Object mockService = null;
              for (Object o : beansOfType.values()) {
                if (o.getClass().getName().endsWith("Mock")) {
                  mockService = o;
                }
              }
              if (mockService == null) {
                throw new AppConfigException(
                    "dubbo消费者:" + field.getType() + " mock实现类不存在,类名必须以Mock为后缀");
              }
              field.set(bean, mockService);
              log.info(
                  "[MOCK]dubbo @Reference {}.{} has bean mocked with {}",
                  field.getDeclaringClass().getSimpleName(),
                  field.getName(),
                  mockService.getClass().getName());
            }
          }
        });
    return bean;
  }
}
