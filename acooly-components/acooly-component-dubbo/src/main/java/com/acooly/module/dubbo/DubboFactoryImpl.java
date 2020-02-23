package com.acooly.module.dubbo;

import com.acooly.core.common.dubbo.DubboFactory;
import com.acooly.core.utils.Asserts;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class DubboFactoryImpl implements DubboFactory, InitializingBean, DisposableBean, ApplicationContextAware {
    private ReferenceConfigCache referenceConfigCache;


    public DubboFactoryImpl(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    private ApplicationContext applicationContext;

    private ConsumerConfig consumerConfig;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getProxy(Class<T> clazz, String group, String version, int timeout) {
        Asserts.notNull(clazz);
        Asserts.notNull(group);
        Asserts.notNull(version);

        ApplicationConfig applicationConfig = applicationContext.getBean(ApplicationConfig.class);
        Map<String, RegistryConfig> registryConfigMap =
                applicationContext.getBeansOfType(RegistryConfig.class);
        if (registryConfigMap == null || registryConfigMap.isEmpty()) {
            throw new RuntimeException("请配置dubbo基本配置");
        }
        ReferenceConfig reference = new ReferenceConfig();
        reference.setApplication(applicationConfig);
        reference.setRegistries(new ArrayList<>(registryConfigMap.values()));
        reference.setInterface(clazz);
        reference.setVersion(version);
        reference.setGroup(group);
        reference.setConsumer(consumerConfig);
        return (T) clazz.cast(referenceConfigCache.get(reference));
    }

    @Override
    public <T> T getProxy(Class<T> clazz, String group, String version) {
        return this.getProxy(clazz, group, version, DEFAUTL_TIMEOUT);
    }

    @Override
    public <T> T getProxy(Class<T> clazz, String group) {
        return this.getProxy(clazz, group, DEFAULT_VERSION, DEFAUTL_TIMEOUT);
    }

    @Override
    public <T> T getProxy(Class<T> clazz) {
        return this.getProxy(clazz, null, DEFAULT_VERSION, DEFAUTL_TIMEOUT);
    }

    @Override
    public void afterPropertiesSet() {
        referenceConfigCache = ReferenceConfigCache.getCache();
    }

    @Override
    public void destroy() {
        if (referenceConfigCache != null) {
            referenceConfigCache.destroyAll();
        }
    }
}
