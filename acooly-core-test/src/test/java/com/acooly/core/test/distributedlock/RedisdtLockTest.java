/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-07-10 11:23 创建
 */
package com.acooly.core.test.distributedlock;

import com.acooly.core.test.TestBase;
import com.acooly.module.distributedlock.DistributedLockFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.Lock;

import static com.acooly.core.common.boot.Apps.SPRING_PROFILE_ACTIVE;

/**
 * @author shuijing
 */
public class RedisdtLockTest extends TestBase {

    static {
        System.setProperty(SPRING_PROFILE_ACTIVE, "qiubo");
    }

    @Autowired
    private DistributedLockFactory factory;

    @Test
    public void testLockUnlock() {
        Lock lock = factory.newLock("lock1");

        lock.lock();
        lock.unlock();

        lock.lock();
        lock.unlock();
    }
}
