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
    }
}
