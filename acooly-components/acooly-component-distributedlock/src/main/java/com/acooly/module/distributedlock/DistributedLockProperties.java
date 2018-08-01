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

    private boolean enable = true;

    /**
     * 默认使用redis模式
     */
    private Mode mode = Mode.Redis;

    private Zookeeper zookeeper = new Zookeeper();
    private Redis redis = new Redis();

    @Data
    public static class Redis {
        /**
         * redis分布式锁，客户端宕机，自动解锁超时时间，默认30秒
         */
        private Long lockWatchdogTimeout = 30 * 1000L;
    }

    @Data
    public static class Zookeeper {
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


    public enum Mode implements Messageable {

        /**
         * redis 实现的分布式锁
         */
        Redis("redis"),

        /**
         * zookeeper InterProcessMutex分布式锁
         */
        Zookeeper("zookeeper");


        private final String code;

        Mode(String code) {
            this.code = code;
        }

        @Override
        public String code() {
            return this.code;
        }
    }

    public interface Messageable {
        String code();
    }
}
