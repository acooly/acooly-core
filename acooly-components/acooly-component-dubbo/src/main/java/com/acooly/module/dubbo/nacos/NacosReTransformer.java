package com.acooly.module.dubbo.nacos;

import com.acooly.core.common.transformer.Retransformer;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class NacosReTransformer implements Retransformer {

    @Override
    public void simpleTransformer( ByteBuddy byteBuddy ) throws Throwable {
        byteBuddy.redefine(NacosDiscoveryPropertiesModify.class)
                .name(NacosDiscoveryProperties.class.getName())
                .make().load(NacosDiscoveryProperties.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());
    }

    @Override
    public AgentBuilder agentTransformer( AgentBuilder agentBuilder ) throws Throwable {
        return null;
    }
}
