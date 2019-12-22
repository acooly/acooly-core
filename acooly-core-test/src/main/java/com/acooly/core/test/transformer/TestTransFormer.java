package com.acooly.core.test.transformer;

import com.acooly.core.common.transformer.Retransformer;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class TestTransFormer implements Retransformer {

    @Override
    public void simpleTransformer( ByteBuddy byteBuddy ) throws Throwable {
        byteBuddy.redefine(TransFormerClass.class)
                .name(OriginClass.class.getName())
                .make().load(OriginClass.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());
    }

    @Override
    public AgentBuilder agentTransformer( AgentBuilder agentBuilder ) throws Throwable {
        return null;
    }
}
