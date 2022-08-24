/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-04-12 23:33 创建
 */
package com.acooly.module.web;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.unit.DataSize;

/**
 * @author qiubo
 * @author zhangpu 2019-06
 */
public class WebComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 文件上传
        setPropertyIfMissing("spring.servlet.multipart.max-file-size", DataSize.ofMegabytes(50).toBytes());
        setPropertyIfMissing("spring.servlet.multipart.max-request-size", DataSize.ofMegabytes(300).toBytes());

        // 设置 Session
        setPropertyIfMissing("server.servlet.session.cookie.name", Apps.getAppSessionCookieName());
        setPropertyIfMissing("server.servlet.session.cookie.httpOnly", Boolean.TRUE.toString());
        setPropertyIfMissing("server.servlet.session.tracking-modes", "cookie");
        // 单位秒，默认设置2小时
        setPropertyIfMissing("server.servlet.session.timeout", 7200);
        setPropertyIfMissing("spring.session.store-type", "REDIS");
        setPropertyIfMissing("spring.session.redis.namespace", "session:" + Apps.getAppName());

        // 静态文件缓存
        if (!Apps.isDevMode()) {
            System.setProperty("spring.resources.cache.period", "-1");
        }
        // 设置FreemarkerProperties的非默认参数
        setPropertyIfMissing("spring.freemarker.request-context-attribute", "rc");
        setPropertyIfMissing("spring.freemarker.prefer-file-system-access", false);
        setPropertyIfMissing("spring.freemarker.cache", true);
        setPropertyIfMissing("spring.freemarker.suffix", ".ftl");
        // 设置Freemarker的参数
        setPropertyIfMissing("spring.freemarker.settings.classic_compatible", true);
        setPropertyIfMissing("spring.freemarker.settings.whitespace_stripping", true);
        setPropertyIfMissing("spring.freemarker.settings.locale", "zh_CN");
        setPropertyIfMissing("spring.freemarker.settings.default_encoding", "utf-8");
        setPropertyIfMissing("spring.freemarker.settings.url_escaping_charset", "utf-8");
        setPropertyIfMissing("spring.freemarker.settings.tag_syntax", "auto_detect");
        setPropertyIfMissing("spring.freemarker.settings.datetime_format", "yyyy-MM-dd HH:mm:ss");
        setPropertyIfMissing("spring.freemarker.settings.date_format", "yyyy-MM-dd");
        setPropertyIfMissing("spring.freemarker.settings.time_format", "HH:mm:ss");
        setPropertyIfMissing("spring.freemarker.settings.number_format", "0.######;");
        setPropertyIfMissing("spring.freemarker.settings.boolean_format", "true,false");
        // 开启freemarker支持?api方式调用对象方法
        // 参考：https://freemarker.apache.org/docs/ref_builtins_expert.html#ref_buitin_api_and_has_api
        setPropertyIfMissing("spring.freemarker.settings.api_builtin_enabled", "true");
        setPropertyIfMissing("spring.freemarker.settings.incompatible_improvements", "2.3.28");
        // 设置 jackson
        setPropertyIfMissing("spring.jackson.date-format", "yyyy-MM-dd HH:mm:ss");
        setPropertyIfMissing("spring.jackson.time-zone", "Asia/Shanghai");
    }
}
