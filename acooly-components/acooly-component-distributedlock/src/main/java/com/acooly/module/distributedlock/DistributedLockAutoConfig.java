package com.acooly.module.distributedlock;

import com.acooly.module.distributedlock.zk.curator.DistributedLockFactory;
import com.acooly.module.dubbo.DubboProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.acooly.module.distributedlock.DistributedLockProperties.PREFIX;

/**
 * @author shuijing
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DistributedLockProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
public class DistributedLockAutoConfig {
    @Bean
    public CuratorFramework curatorFramework(DistributedLockProperties lockProperties, DubboProperties dubboProperties) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(dubboProperties.getZkUrl())
                .retryPolicy(
                        new RetryNTimes(lockProperties.getRetryTimes(), lockProperties.getSleepMsBetweenRetries()))
                .connectionTimeoutMs(lockProperties.getConnectionTimeoutMs())
                .sessionTimeoutMs(lockProperties.getSessionTimeoutMs());
        CuratorFramework curatorFramework = builder.build();
        curatorFramework.start();
        return curatorFramework;
    }

    @Bean
    public DistributedLockFactory distributedLockFactory() {
        return new DistributedLockFactory();
    }

}
