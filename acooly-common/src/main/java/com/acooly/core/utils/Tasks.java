/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-01-16 23:53
 */
package com.acooly.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhangpu
 * @date 2020-01-16 23:53
 */
@Slf4j
public class Tasks {

    /**
     * 指定多个线程同时运行一个任务，测试并发性
     *
     * @param nThreads
     * @param task
     * @return 返回任务执行耗时
     * @throws InterruptedException
     */
    public static long concurrentTasks(int nThreads, final Runnable task) throws InterruptedException {
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
        log.info("runTasks 线程准备完成 threads:{} ...", nThreads);
        endGate.await();
        long end = System.currentTimeMillis();
        long times = end - start;
        log.info("runTasks 线程执行完成 threads:{}, times(ms): {}", nThreads, times);
        return times;
    }
}
