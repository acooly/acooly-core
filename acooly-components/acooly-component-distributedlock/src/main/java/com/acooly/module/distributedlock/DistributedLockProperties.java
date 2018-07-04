package com.acooly.module.distributedlock;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shuijing
 */
@ConfigurationProperties(DistributedLockProperties.PREFIX)
@Data
public class DistributedLockProperties {

    public static final String PREFIX = "acooly.distributedlock";
    private boolean enable;

    /**
     * zk连接故障时重试次数
     */
    private int retryTimes = 1000;
    /**
     * zk session timeout ，单位ms
     */
    private int sessionTimeoutMs = 30000;
    /**
     * zk connection timeout ，单位ms
     */
    private int connectionTimeoutMs = 10000;

    /**
     * zk连接异常时，重试时间间隔
     */
    private int sleepMsBetweenRetries = 1000;
}
