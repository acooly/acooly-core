/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-04-12 23:33 创建
 */
package com.acooly.core.common.boot.component.web;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo@yiji.com
 */
public class WebComponentInitializer implements ComponentInitializer {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		setPropertyIfMissing("multipart.maxFileSize", "100Mb");
        setPropertyIfMissing("multipart.maxFileSize", "300Mb");

        setPropertyIfMissing("spring.freemarker.expose-session-attributes","false");
        setPropertyIfMissing("spring.freemarker.expose-request-attributes","false");
        setPropertyIfMissing("spring.freemarker.allow-session-override","false");
        setPropertyIfMissing("spring.freemarker.request-context-attribute","rc");
        setPropertyIfMissing("spring.freemarker.request-context-attribute","rc");
        setPropertyIfMissing("spring.freemarker.request-context-attribute","rc");


        // 设置session
        setPropertyIfMissing("server.session.cookie.name", Apps.getAppSessionCookieName());
        setPropertyIfMissing("server.session.cookie.httpOnly", Boolean.TRUE.toString());
        setPropertyIfMissing("server.session.tracking-modes", "cookie");
        setPropertyIfMissing("server.session.timeout","3600");
        setPropertyIfMissing("spring.session.store-type","REDIS");
        setPropertyIfMissing("spring.redis.pool.max-active","100");
    }
}
