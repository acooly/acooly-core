package com.acooly.module.distributedlock.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;

public class DistributedLockFactory {
    public static final String ACOOLY_DISTRIBUTE_LOCK = "/acooly/distributeLock/";
    @Autowired
    private CuratorFramework curatorFramework;

    public ZkDistributedLock newLock(String resource) {
        notNull(resource, "resource不能为空");
        String lockPath = ACOOLY_DISTRIBUTE_LOCK + resource;
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, lockPath);
        ZkDistributedLock zkDistributedLock = new ZkDistributedLock(mutex, lockPath);
        return zkDistributedLock;
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
