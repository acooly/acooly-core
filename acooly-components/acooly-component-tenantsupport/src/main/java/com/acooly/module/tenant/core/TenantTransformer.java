package com.acooly.module.tenant.core;

import com.acooly.core.common.transformer.Retransformer;
import com.acooly.module.cache.DefaultKeySerializer;
import com.acooly.module.ds.JDBCAutoConfig;
import com.acooly.module.dubbo.MDCFilter;
import com.acooly.module.dubbo.RequestContextFilter;
import com.acooly.module.event.EventBus;
import com.acooly.module.scheduler.SchedulerAutoConfig;
import com.acooly.module.scheduler.executor.LocalTaskExecutor;
import com.acooly.module.scheduler.job.QuartzJob;
import com.acooly.module.tenant.cache.DefaultKeySerializerX;
import com.acooly.module.tenant.ds.JDBCAutoConfigX;
import com.acooly.module.tenant.dubbo.MDCFilterX;
import com.acooly.module.tenant.dubbo.RequestContextFilterX;
import com.acooly.module.tenant.event.AdviceX;
import com.acooly.module.tenant.event.EventBusX;
import com.acooly.module.tenant.scheduler.LocalTaskExecutorX;
import com.acooly.module.tenant.scheduler.QuartzJobX;
import com.acooly.module.tenant.scheduler.SchedulerAutoConfigX;
import com.acooly.module.tenant.threadpool.ThreadPoolAutoConfigX;
import com.acooly.module.threadpool.ThreadPoolAutoConfig;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import net.engio.mbassy.bus.MessagePublication;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class TenantTransformer implements Retransformer {

    @Override
    public void simpleTransformer( ByteBuddy byteBuddy ) throws Throwable {

        byteBuddy.redefine(DefaultKeySerializerX.class).name(DefaultKeySerializer.class.getName())
                .make().load(DefaultKeySerializer.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(MDCFilterX.class).name(MDCFilter.class.getName())
                .make().load(MDCFilter.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(RequestContextFilterX.class).name(RequestContextFilter.class.getName())
                .make().load(RequestContextFilter.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(EventBusX.class).name(EventBus.class.getName())
                .make()
                .load(EventBus.class.getClassLoader(),
                        ClassReloadingStrategy.fromInstalledAgent());
        byteBuddy.redefine(JDBCAutoConfigX.class).name(JDBCAutoConfig.class.getName())
                .make().load(JDBCAutoConfig.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(ThreadPoolAutoConfigX.class).name(ThreadPoolAutoConfig.class.getName())
                .make().load(ThreadPoolAutoConfig.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(LocalTaskExecutorX.class).name(LocalTaskExecutor.class.getName())
                .make().load(LocalTaskExecutor.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(QuartzJobX.class).name(QuartzJob.class.getName())
                .make().load(QuartzJob.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(SchedulerAutoConfigX.class).name(SchedulerAutoConfig.class.getName())
                .make().load(SchedulerAutoConfig.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());


    }

    @Override
    public AgentBuilder agentTransformer( AgentBuilder agentBuilder ) throws Throwable {
        return agentBuilder.type(ElementMatchers.is(MessagePublication.class))
                .transform(( b, t, c, j ) ->
                        b.visit(Advice.to(AdviceX.class).on(ElementMatchers.named("execute"))));

    }
}
