/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-06 15:41 创建
 *
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.dubbo.DubboFactory;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.module.dubbo.mock.DubboMockBeanPostProcessor;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({DubboProperties.class})
@Import(DubboImportBeanDefinitionRegistrar.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "acooly.dubbo.enable", matchIfMissing = true)
public class DubboAutoConfig implements InitializingBean {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DubboAutoConfig.class);

    private static DubboProperties dubboProperties;

    private static void initDubboProperties() {
        try {
            if (dubboProperties == null) {
                DubboProperties tmp = new DubboProperties();
                EnvironmentHolder.buildProperties(tmp);
                tmp.afterPropertiesSet();
                DubboAutoConfig.dubboProperties = tmp;
            }
        } catch (Exception e) {
            throw new AppConfigException("dubbo配置错误", e);
        }
    }

    @Bean
    public static ApplicationConfig applicationConfig() {
        initDubboProperties();
        ApplicationConfig config = new ApplicationConfig();
        config.setName(Apps.getAppName());
        config.setOwner(dubboProperties.getOwner());
        Apps.exposeInfo("dubbo.owner", config.getOwner());
        if (!Strings.isNullOrEmpty(dubboProperties.getVersion())) {
            config.setVersion(dubboProperties.getVersion());
        }
        return config;
    }

    @Bean
    @DependsOn("applicationConfig")
    public static RegistryConfig registryConfig() {
        initDubboProperties();
        RegistryConfig config = new RegistryConfig();
        config.setProtocol("zookeeper");
        logger.info(
                "dubbo使用注册中心地址:{}, 是否注册:{}", dubboProperties.getZkUrl(), dubboProperties.isRegister());
        config.setRegister(dubboProperties.isRegister());
        config.setAddress(dubboProperties.getZkUrl());
        if (!Apps.isRunInTest()) {
            config.setFile(Apps.getAppDataPath() + "/dubbo/dubbo.cache");
        }
        config.setClient("zkclient");
        config.setCluster("failfast");
        return config;
    }

    @Bean
    @ConditionalOnProperty(value = "acooly.dubbo.refOnlyZkUrl1")
    @DependsOn("applicationConfig")
    public static RegistryConfig refOnlyRegistryConfig1() {
        initDubboProperties();
        final String refOnlyZkUrl1 = dubboProperties.getRefOnlyZkUrl1();
        if (StringUtils.isBlank(refOnlyZkUrl1)) {
            throw new AppConfigException("acooly.dubbo.refOnlyZkUrl1 属性的值不能为空!");
        }
        return createRefOnlyRegistry(refOnlyZkUrl1, "1");
    }

    @Bean
    @ConditionalOnProperty(value = "acooly.dubbo.refOnlyZkUrl2")
    @DependsOn("applicationConfig")
    public static RegistryConfig refOnlyRegistryConfig2() {
        initDubboProperties();
        final String refOnlyZkUrl2 = dubboProperties.getRefOnlyZkUrl2();
        if (StringUtils.isBlank(refOnlyZkUrl2)) {
            throw new AppConfigException("acooly.dubbo.refOnlyZkUrl2 属性的值不能为空!");
        }
        return createRefOnlyRegistry(refOnlyZkUrl2, "2");
    }

    @Bean
    @ConditionalOnProperty(value = "acooly.dubbo.refOnlyZkUrl3")
    @DependsOn("applicationConfig")
    public static RegistryConfig refOnlyRegistryConfig3() {
        initDubboProperties();
        final String refOnlyZkUrl3 = dubboProperties.getRefOnlyZkUrl3();
        if (StringUtils.isBlank(refOnlyZkUrl3)) {
            throw new AppConfigException("acooly.dubbo.refOnlyZkUrl3 属性的值不能为空!");
        }
        return createRefOnlyRegistry(refOnlyZkUrl3, "3");
    }

    private static RegistryConfig createRefOnlyRegistry(String zkUrl, String id) {
        RegistryConfig config = new RegistryConfig();
        config.setProtocol("zookeeper");
        String cacheFile = Apps.getAppDataPath() + "/dubbo/dubbo.cache.refOnly" + id;

        logger.info("dubbo使用注册中心(只消费)地址:{}", zkUrl);
        config.setAddress(zkUrl);
        if (!Apps.isRunInTest()) {
            config.setFile(cacheFile);
        }
        config.setRegister(false);
        return config;
    }

    @Bean
    @DependsOn({"applicationConfig", "registryConfig"})
    public static ConsumerConfig consumerConfig() {
        initDubboProperties();
        ConsumerConfig config = new ConsumerConfig();
        config.setCheck(false);
        config.setLoadbalance("roundrobin");
        if (dubboProperties.isConsumerLog()) {
            config.setFilter("consumerLogFilter");
        }
        config.setClient("netty4");
        config.setCluster("failfast");
        return config;
    }

    @Bean
    @DependsOn({"registryConfig"})
    @ConditionalOnProperty(
            name = {"acooly.devMode", "acooly.dubbo.monitor.disable"},
            havingValue = "false",
            matchIfMissing = true
    )
    public static MonitorConfig monitorConfig() {
        MonitorConfig config = new MonitorConfig();
        config.setProtocol("registry");
        return config;
    }

    @Bean
    public static DubboMockBeanPostProcessor dubboMockBeanPostProcessor() {
        DubboMockBeanPostProcessor config = new DubboMockBeanPostProcessor();
        initDubboProperties();
        config.setAnnotationPackage(Apps.getBasePackage());
        config.setMockInterfaces(dubboProperties.getConsumer().getMockInterfaces());
        return config;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Bean
    public DubboRemoteProxyFacotry dubboRemoteProxyFacotry() {
        return new DubboRemoteProxyFacotry();
    }

    @Bean
    public DubboFactory dubboFactory(DubboRemoteProxyFacotry dubboRemoteProxyFacotry) {
        return new DubboFactoryImpl(dubboRemoteProxyFacotry);
    }

    @Bean
    public DubboShutdownApplicationListener dubboShutdownApplicationListener() {
        return new DubboShutdownApplicationListener();
    }
}
