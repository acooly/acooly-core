/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-06-17 17:51 创建
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.Ids;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Ids分布式Id单元测试
 *
 * @author zhangpu@acooly.cn
 */
public class IdsTest {

    public static final Logger log = LoggerFactory.getLogger(IdsTest.class);

    final int threads = 100;
    final int timesPerThread = 100;
    final Set<String> container = Collections.synchronizedSet(new HashSet<String>());

    @Test
    public void testBatchIds() throws Exception {
        AtomicLong successCounter = new AtomicLong();
        long timeTasks = runTasks(threads, new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < timesPerThread; j++) {
                    String id = Ids.getDid();
                    if (container.add(id)) {
                        successCounter.incrementAndGet();
                    } else {
                        log.info("重复Id: {}", id);
                    }
                }
            }
        });
        log.info("Ids Batch test -> threads:{}, count:{} success:{}, ms:{}", threads, threads * timesPerThread, successCounter.longValue(), timeTasks);
    }


    /**
     * 指定多个线程同时运行一个任务，测试并发性
     *
     * @param nThreads
     * @param task
     * @return
     * @throws InterruptedException
     */
    public long runTasks(int nThreads, final Runnable task) throws InterruptedException {
        // 真正的测试
        // 使用同步工具类，保证多个线程同时（近似同时）执行
        final CountDownLatch startGate = new CountDownLatch(1);
        // 使用同步工具类，用于等待所有线程都运行结束时，再统计耗时
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        log.debug("task await: {}", Thread.currentThread().getName());
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        log.warn("多线同步执行失败", e);
                    }
                }
            };
            t.setName("task_" + i);
            t.start();
        }
        startGate.countDown();
        long start = System.currentTimeMillis();
        log.info("runTasks 线程准备完成 threads:{}", nThreads);
        endGate.await();
        long end = System.currentTimeMillis();
        long times = end - start;
        log.info("runTasks 线程执行完成 threads:{}, times: {}", nThreads, times);
        return times;
    }
}
