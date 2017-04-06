package com.acooly.module.scheduler;

import com.acooly.core.common.boot.component.ComponentInitializer;
import com.acooly.core.utils.ShutdownHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author shuijing
 */
public class SchedulerComponentInitializer implements ComponentInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerComponentInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        setPropertyIfMissing("org.quartz.scheduler.instanceName", "scheduler");
        setPropertyIfMissing("org.quartz.scheduler.instanceId", "AUTO");
        setPropertyIfMissing("org.quartz.threadPool.threadCount", 10);
        setPropertyIfMissing("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        setPropertyIfMissing("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        setPropertyIfMissing("org.quartz.jobStore.useProperties", true);
        setPropertyIfMissing("org.quartz.jobStore.misfireThreshold", 60000);
        setPropertyIfMissing("org.quartz.jobStore.tablePrefix", "QRTZ_");
        setPropertyIfMissing("org.quartz.jobStore.isClustered", true);
        setPropertyIfMissing("org.quartz.jobStore.clusterCheckinInterval", 20000);
        setPropertyIfMissing("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
        setPropertyIfMissing("org.quartz.plugin.shutdownhook.cleanShutdown", true);
    }
}
