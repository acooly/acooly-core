package com.acooly.module.tenant.core;

import com.acooly.core.common.transformer.Retransformer;
import com.acooly.core.utils.StringUtils;
import com.acooly.module.cache.DefaultKeySerializer;
import com.acooly.module.ds.JDBCAutoConfig;
import com.acooly.module.dubbo.MDCFilter;
import com.acooly.module.dubbo.RequestContextFilter;
import com.acooly.module.event.EventBus;
import com.acooly.module.ofile.OFileComponentInitializer;
import com.acooly.module.scheduler.SchedulerAutoConfig;
import com.acooly.module.scheduler.executor.DubboTaskExecutor;
import com.acooly.module.scheduler.executor.LocalTaskExecutor;
import com.acooly.module.scheduler.job.QuartzJob;
import com.acooly.module.tenant.cache.DefaultKeySerializerX;
import com.acooly.module.tenant.ds.JDBCAutoConfigX;
import com.acooly.module.tenant.dubbo.MDCFilterX;
import com.acooly.module.tenant.dubbo.RequestContextFilterX;
import com.acooly.module.tenant.event.AdviceX;
import com.acooly.module.tenant.event.EventBusX;
import com.acooly.module.tenant.ofile.FileAdviceX;
import com.acooly.module.tenant.ofile.OFileComponentInitializerX;
import com.acooly.module.tenant.scheduler.DubboTaskExecutorX;
import com.acooly.module.tenant.scheduler.LocalTaskExecutorX;
import com.acooly.module.tenant.scheduler.QuartzJobX;
import com.acooly.module.tenant.scheduler.SchedulerAutoConfigX;
import com.acooly.module.tenant.security.CheckAdviceX;
import com.acooly.module.tenant.security.ExeLoginAdviceX;
import com.acooly.module.tenant.security.IndexAdviceX;
import com.acooly.module.tenant.security.LoginAdviceX;
import com.acooly.module.tenant.security.ShiroFilterAdviceX;
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
        if (!checkEnable()) {
            return;
        }
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

        byteBuddy.redefine(DubboTaskExecutorX.class).name(DubboTaskExecutor.class.getName())
                .make().load(DubboTaskExecutor.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(QuartzJobX.class).name(QuartzJob.class.getName())
                .make().load(QuartzJob.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(SchedulerAutoConfigX.class).name(SchedulerAutoConfig.class.getName())
                .make().load(SchedulerAutoConfig.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(OFileComponentInitializerX.class)
                .name(OFileComponentInitializer.class.getName())
                .make().load(OFileComponentInitializer.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());


    }


    @Override
    public AgentBuilder agentTransformer( AgentBuilder agentBuilder ) throws Throwable {
        if (!checkEnable()) {
            return agentBuilder;
        }
        return agentBuilder
                .type(ElementMatchers.named(AdviceX.CLASS))
                .transform(( b, t, c, j ) ->
                        b.visit(Advice.to(AdviceX.class).on(ElementMatchers.named("execute"))))
                .type(ElementMatchers
                        .named(LoginAdviceX.CLASS))
                .transform(( b, t, c, j ) -> b.visit(Advice.to(LoginAdviceX.class)
                        .on(ElementMatchers.named("executeLogin"))))
                .type(ElementMatchers
                        .named(CheckAdviceX.CLASS))
                .transform(( b, t, c, j ) -> b.visit(Advice.to(CheckAdviceX.class)
                        .on(ElementMatchers.named("isAccessAllowed"))))
                .type(ElementMatchers
                        .named(FileAdviceX.CLASS))
                .transform(( b, t, c, j ) -> b.visit(Advice.to(FileAdviceX.class)
                        .on(ElementMatchers.named("getStorageRoot"))))
                .type(ElementMatchers
                        .named(ExeLoginAdviceX.CLASS))
                .transform(( b, t, c, j ) -> b.visit(Advice.to(ExeLoginAdviceX.class)
                        .on(ElementMatchers.named("login"))))
                .type(ElementMatchers
                        .named(IndexAdviceX.CLASS))
                .transform(( b, t, c, j ) -> b.visit(Advice.to(IndexAdviceX.class)
                        .on(ElementMatchers.named("index"))))
                .type(ElementMatchers
                        .named(ShiroFilterAdviceX.CLASS))
                .transform(( b, t, c, j ) -> b.visit(Advice.to(ShiroFilterAdviceX.class)
                        .on(ElementMatchers.named("doFilter"))));


    }

    private boolean checkEnable() {
        String enable = System.getProperty("tenant.enable");
        if (!StringUtils.isEmpty(enable) && Boolean.valueOf(enable).equals(false)) {
            return false;
        }
        return true;
    }
}
