package com.acooly.module.tenant.core;

import com.acooly.core.common.boot.log.LogAutoConfig;
import com.acooly.core.common.transformer.Retransformer;
import com.acooly.module.cache.DefaultKeySerializer;
import com.acooly.module.ds.JDBCAutoConfig;
import com.acooly.module.dubbo.MDCFilter;
import com.acooly.module.dubbo.RequestContextFilter;
import com.acooly.module.event.EventBus;
import com.acooly.module.event.ExMessageDispatcher;
import com.acooly.module.tenant.cache.DefaultKeySerializerX;
import com.acooly.module.tenant.ds.JDBCAutoConfigX;
import com.acooly.module.tenant.dubbo.MDCFilterX;
import com.acooly.module.tenant.dubbo.RequestContextFilterX;
import com.acooly.module.tenant.event.EventBusX;
import com.acooly.module.tenant.event.ExMessageDispatcherX;
import com.acooly.module.tenant.log.LogAutoConfigX;
import com.acooly.module.tenant.threadpool.ThreadPoolAutoConfigX;
import com.acooly.module.threadpool.ThreadPoolAutoConfig;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

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

        byteBuddy.redefine(ExMessageDispatcherX.class).name(ExMessageDispatcher.class.getName())
                .make().load(ExMessageDispatcher.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(JDBCAutoConfigX.class).name(JDBCAutoConfig.class.getName())
                .make().load(JDBCAutoConfig.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(LogAutoConfigX.class).name(LogAutoConfig.class.getName())
                .make().load(LogAutoConfig.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

        byteBuddy.redefine(ThreadPoolAutoConfigX.class).name(ThreadPoolAutoConfig.class.getName())
                .make().load(ThreadPoolAutoConfig.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());

    }

}
