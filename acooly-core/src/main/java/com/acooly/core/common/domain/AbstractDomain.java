package com.acooly.core.common.domain;

import com.acooly.core.common.boot.ApplicationContextHolder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanFactory;

import javax.persistence.MappedSuperclass;
import java.util.List;

/**
 * @author qiubo@yiji.com
 */
@MappedSuperclass
@Getter
@Setter
public class AbstractDomain extends AbstractEntity {
    private static volatile AutowiredAnnotationBeanPostProcessor
            autowiredAnnotationBeanPostProcessor = null;

    private static boolean inited = false;

    public AbstractDomain() {
        if (inited) {
            autowire();
        }
    }

    public static void inited() {
        inited = true;
    }

    private void autowire() {
        if (autowiredAnnotationBeanPostProcessor == null) {
            synchronized (AbstractEntity.class) {
                if (autowiredAnnotationBeanPostProcessor == null) {
                    List<BeanPostProcessor> beanPostProcessors =
                            ((AbstractBeanFactory) ApplicationContextHolder.get().getBeanFactory())
                                    .getBeanPostProcessors();
                    for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                        if (beanPostProcessor instanceof AutowiredAnnotationBeanPostProcessor
                                && beanPostProcessor
                                .getClass()
                                .getName()
                                .contains("AutowiredAnnotationBeanPostProcessor")) {
                            autowiredAnnotationBeanPostProcessor =
                                    (AutowiredAnnotationBeanPostProcessor) beanPostProcessor;
                        }
                    }
                }
            }
        }
        autowiredAnnotationBeanPostProcessor.postProcessProperties(null,this, getClass().getName());
    }
}
