/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.core.utils.conversion.collection.ref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 用做 ReferenceMap 的清除引用的引用队列。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class FinalizableReferenceQueue extends ReferenceQueue<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinalizableReferenceQueue.class);

    private static final Object LOCK = new Object();

    private static final ReadWriteLock THREAD_LOCK = new ReentrantReadWriteLock();

    private static final Lock READ_LOCK = THREAD_LOCK.readLock();

    private static final Lock WRITE_LOCK = THREAD_LOCK.writeLock();

    private static final Set<CleanUpThread> THREADS =
            Collections.newSetFromMap(new WeakHashMap<CleanUpThread, Boolean>());

    private static int i;

    private final WeakReference<CleanUpThread> threadRederence;

    /**
     * 构造一个新的清除引用的引用队列。
     */
    public FinalizableReferenceQueue() {
        this(getThreadName());
    }

    /**
     * 构造一个新的清除引用的引用队列。
     *
     * @param name 引用队列的名称，该名称用做清理的守护线程的名称。
     */
    public FinalizableReferenceQueue(String name) {
        CleanUpThread thread = newCleanUpThread(name);
        this.threadRederence = new WeakReference<CleanUpThread>(thread);
        start(thread);
    }

    private static String getThreadName() {
        synchronized (LOCK) {
            return "FinalizableReferenceQueue#" + ++i;
        }
    }

    /**
     * 得到 FinalizableReferenceQueue 的单例。
     *
     * @return FinalizableReferenceQueue 的单例。
     */
    public static FinalizableReferenceQueue getInstance() {
        return FinalizeReferenceQueueInstance.INSTANCE;
    }

    /**
     * 得到所有 FinalizableReferenceQueue 创建的清理线程。
     *
     * @return 所有 FinalizableReferenceQueue 创建的清理线程。
     */
    public static Set<CleanUpThread> getCleanUpThreads() {
        READ_LOCK.lock();
        try {
            return new LinkedHashSet<CleanUpThread>(THREADS);
        } finally {
            READ_LOCK.unlock();
        }
    }

    /**
     * 创建一个新的 CleanUpThread 用于监视清理。
     *
     * <p>通常覆盖了该方法应该覆盖 {@link #start(CleanUpThread)} 方法改变开始的方式。
     *
     * @param name 清理线程的名称。
     * @return 合适的 CleanUpThread 的实例。
     * @see #start(CleanUpThread)
     */
    protected CleanUpThread newCleanUpThread(String name) {
        CleanUpThread thread = new CleanUpThread(name, this);
        return thread;
    }

    /**
     * 得到 当前队列清理时所用的清理监视器线程。
     *
     * @return 当前队列清理时所用的清理监视器线程，如果线程已经被中断或者执行完毕则可能返回 null 。
     */
    public CleanUpThread getCleanUpThread() {
        return this.threadRederence == null ? null : this.threadRederence.get();
    }

    /**
     * 执行引用的清除工作。
     *
     * @param reference 要执行清除工作的引用。
     */
    protected void cleanUp(Reference<?> reference) {
        try {
            ((FinalizableReference<?>) reference).finalizeReferent();
        } catch (Throwable t) {
            LOGGER.error("清除引用时发生错误", t);
        }
    }

    /**
     * 开始垃圾回收引用监视。
     *
     * <p>该实现中 thread 的 {@link Thread#setDaemon(boolean)} 在这里设置。
     *
     * @param thread 监视器线程。
     */
    protected void start(CleanUpThread thread) {
        thread.setDaemon(true);
        thread.start();
        WRITE_LOCK.lock();
        try {
            THREADS.add(thread);
        } finally {
            WRITE_LOCK.unlock();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("垃圾回收引用监视器线程[" + thread.getName() + "]开始工作。");
        }
    }

    private static class FinalizeReferenceQueueInstance {
        private static final FinalizableReferenceQueue INSTANCE =
                new FinalizableReferenceQueue("FinalizableReferenceQueue");
    }

    /**
     * 清理守护线程。
     *
     * @author Agreal·Lee (e-mail:lixiang@yiji.com)
     */
    public static class CleanUpThread extends Thread {

        private WeakReference<FinalizableReferenceQueue> queueRef;

        private CleanUpThread(String name, FinalizableReferenceQueue finalizableReferenceQueue) {
            super(name);
            this.queueRef = new WeakReference<FinalizableReferenceQueue>(finalizableReferenceQueue);
        }

        /**
         * 得到 当前线程监视的 FinalizableReferenceQueue 。
         *
         * @return 当前线程监视的 FinalizableReferenceQueue ，如果线程已经被中断或者执行完毕则可能返回 null。
         */
        public FinalizableReferenceQueue getFinalizableReferenceQueue() {
            return this.queueRef == null ? null : this.queueRef.get();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        FinalizableReferenceQueue queue = getFinalizableReferenceQueue();
                        if (queue == null) {
                            return;
                        }
                        queue.cleanUp(queue.remove());
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                        this.queueRef = null;
                        LOGGER.info("垃圾回收引用监视器线程 [" + getName() + "] 被中断。");
                        return;
                    }
                }
            } finally {
                WRITE_LOCK.lock();
                try {
                    THREADS.remove(this);
                } finally {
                    WRITE_LOCK.unlock();
                }
                LOGGER.info("垃圾回收引用监视器线程 [" + getName() + "] 运行结束。");
            }
        }
    }
}
