package com.acooly.core.common.boot.listener;

import com.acooly.core.common.domain.AbstractDomain;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author qiubo@yiji.com
 */
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        AbstractDomain.inited();
    }
}
