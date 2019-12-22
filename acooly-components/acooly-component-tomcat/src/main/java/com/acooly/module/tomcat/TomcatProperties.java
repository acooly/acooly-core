/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-25 16:19 创建
 *
 */
package com.acooly.module.tomcat;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * @author qiubo
 */
@ConfigurationProperties("acooly.tomcat")
@Data
public class TomcatProperties {

    public static final String HTTP_ACCESS_LOG_FORMAT =
            "%h %l [%{yyyy-MM-dd HH:mm:ss.SSS}t] \"%r\" %s %b %D";

    /**
     * 可选：最小空闲线程
     */
    private int minSpareThreads = 20;
    /**
     * 可选：最大线程数
     */
    private volatile int maxThreads = 400;

    private int acceptCount = 100;

    /**
     * 可选：是否启用访问日志
     */
    private boolean accessLogEnable = false;

    /**
     * 可选: 设置uri编码
     */
    private String uriEncoding = "UTF-8";

    /**
     * 可选: 通过外部配置自定义tomcat端口
     */
    private Integer port = null;


    private Duration backgroundProcessorDelay = Duration.ofSeconds(10);

    /**
     * 40X状态返回的页面
     */
    private String error40XPage;
    /**
     * 50X状态返回的页面
     */
    private String error50XPage;

    private Security security = new Security();


    @Getter
    @Setter
    public static class Security {
        /**
         * 是否开启禁用非安全方法的访问
         */
        private boolean enable = true;

        /**
         * 访问URL-PATTERN, 默认全部
         */
        private String pattern = "/*";

        private List<String> omittedMethods = Lists.newArrayList("GET", "get", "POST", "post");

    }

}
