package com.acooly.core.common.transformer;

import com.acooly.core.common.boot.Apps;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Default;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * @author xi
 * @description
 */
public class AgentInstRegListener implements SpringApplicationRunListener, PriorityOrdered {

    /**
     * 用于检测是否使用了spring cloud 了防止被spring cloud 的bootstrap 初始化后在被spring boot 初始化一次
     */
    private static boolean cloudEnv = false;

    /**
     * 用于class redefine
     */
    private static List<Retransformer> retransformers;

    public AgentInstRegListener( SpringApplication application, String[] args ) {
        cloudEnv = Apps.checkCloudEnv(application);
    }

    @Override
    public void starting() {
        if (!cloudEnv) {
            retransformers = SpringFactoriesLoader
                    .loadFactories(Retransformer.class,
                            Thread.currentThread().getContextClassLoader());
            if (retransformers != null) {
                ByteBuddy byteBuddy = new ByteBuddy();
                AgentBuilder builder = new Default(byteBuddy)
                        .with(TypeStrategy.Default.REDEFINE)
                        .with(RedefinitionStrategy.RETRANSFORMATION);
                retransformers.stream().forEach(
                        reTransformer -> {
                            try {
                                reTransformer.simpleTransformer(byteBuddy);
                                AgentBuilder agentBuilder = reTransformer.agentTransformer(builder);
                                if (null != agentBuilder) {
                                    agentBuilder.installOnByteBuddyAgent();
                                }
                            } catch (Throwable e) {
                                throw new RuntimeException("class 转换失败", e);
                            }
                        }
                );
                Apps.byteBuddy = byteBuddy;
            }

        }

    }

    @Override
    public void environmentPrepared( ConfigurableEnvironment environment ) {

    }

    @Override
    public void contextPrepared( ConfigurableApplicationContext context ) {

    }

    @Override
    public void contextLoaded( ConfigurableApplicationContext context ) {

    }

    @Override
    public void started( ConfigurableApplicationContext context ) {

    }

    @Override
    public void running( ConfigurableApplicationContext context ) {

    }

    @Override
    public void failed( ConfigurableApplicationContext context, Throwable exception ) {

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
