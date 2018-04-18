package com.acooly.module.dubbo;

import com.acooly.core.common.boot.Apps;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({DubboProperties.class})
@AutoConfigureAfter(DubboAutoConfig.class)
@ConditionalOnProperty(value = "acooly.dubbo.enable", matchIfMissing = true)
public class DubboProviderAutoConfig {
    private static final Logger logger = LoggerFactory.getLogger(DubboProviderAutoConfig.class);

    @Bean
    @ConditionalOnProperty(value = "acooly.dubbo.provider.enable", matchIfMissing = true)
    @DependsOn("applicationConfig")
    public static ProtocolConfig protocolConfig(DubboProperties dubboProperties) {
        ProtocolConfig config = new ProtocolConfig();
        config.setName("dubbo");
        config.setPort(dubboProperties.getProvider().getPort());
        Apps.exposeInfo("dubbo.port", dubboProperties.getProvider().getPort());
        //配置线程池
        config.setThreadpool("exDubboThreadPool");
        config.setThreads(dubboProperties.getProvider().getMaxThreads());
        //如果当queue设置为0时,会使用SynchronousQueue,这个东东导致了任务线程执行"不均衡"
        //但是如果queue设置得太小,导致queue成为瓶颈,这个时候线程比较闲还出现请求被拒绝的问题
        int queueSize = dubboProperties.getProvider().getQueue();
        if (queueSize != 0) {
            queueSize = Math.max(queueSize, dubboProperties.getProvider().getMaxThreads() / 2);
        }
        config.setQueues(queueSize);
        Map<String, String> params = Maps.newHashMap();
        params.put(
                Constants.CORE_THREADS_KEY, dubboProperties.getProvider().getCorethreads().toString());
        config.setParameters(params);
        //设置序列化协议,如果不设置,使用dubbo默认协议 hessian2
        if (!Strings.isNullOrEmpty(dubboProperties.getProvider().getSerialization())) {
            if ("hessian3".equals(dubboProperties.getProvider().getSerialization())) {
                logger.info("dubbo启用数据压缩特性");
            }
            config.setSerialization(dubboProperties.getProvider().getSerialization());
        }
        return config;
    }

    @Bean
    @ConditionalOnProperty(value = "acooly.dubbo.provider.enable", matchIfMissing = true)
    public static ProviderConfig providerConfig(DubboProperties dubboProperties) {
        ProviderConfig config = new ProviderConfig();
        config.setTimeout(dubboProperties.getProvider().getTimeout());
        config.setCluster("failfast");
        config.setRegister(dubboProperties.getProvider().isRegister());
        //设置延迟暴露,dubbo会用另外一个线程来暴露服务,加快启动过程
        config.setDelay(1);
        if (dubboProperties.isProviderLog()) {
            config.setFilter("providerLogFilter");
        }
        String providerIp = Apps.getEnvironment().getProperty("dubbo.provider.ip");
        if (!Strings.isNullOrEmpty(providerIp)) {
            config.setHost(providerIp);
        }
        return config;
    }
}
