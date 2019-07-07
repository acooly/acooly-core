package com.acooly.module.defence;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo@yiji.com
 */
public class DefenceComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        setPropertyIfMissing("acooly.security.csrf.exclusions.sso[0]", "/role/facade/*");
        setPropertyIfMissing("acooly.security.csrf.exclusions.sso[1]", "/security/config/index.html");
    }
}
