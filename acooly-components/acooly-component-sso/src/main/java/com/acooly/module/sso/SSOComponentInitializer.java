package com.acooly.module.sso;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import com.acooly.core.utils.ShutdownHooks;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author shuijing
 */
@Slf4j
public class SSOComponentInitializer implements ComponentInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //静态文件缓存
        if (!Apps.isDevMode()){
            System.setProperty("spring.resources.cache-period", "-1");
        }
    }
}
