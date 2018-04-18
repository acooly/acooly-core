/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-03-01 16:01 创建
 */
package com.acooly.module.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.threadpool.ThreadPool;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * @author qiubo@yiji.com
 */
public class ExDubboThreadPool implements ThreadPool {

    @Override
    public Executor getExecutor(URL url) {
        //使用固定线程池
        String name = url.getParameter(Constants.THREAD_NAME_KEY, Constants.DEFAULT_THREAD_NAME);
        //		int cores = url.getParameter(Constants.CORE_THREADS_KEY, Constants.DEFAULT_CORE_THREADS);
        int threads = url.getParameter(Constants.THREADS_KEY, Integer.MAX_VALUE);
        int queues = url.getParameter(Constants.QUEUES_KEY, Constants.DEFAULT_QUEUES);
        //		int alive = url.getParameter(Constants.ALIVE_KEY, Constants.DEFAULT_ALIVE);
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(
                        threads,
                        threads,
                        0,
                        TimeUnit.MILLISECONDS,
                        queues == 0
                                ? new SynchronousQueue<>()
                                : (queues < 0 ? new LinkedBlockingQueue<>() : new ArrayBlockingQueue<>(queues)),
                        new NamedThreadFactory(name, true));
        return threadPoolExecutor;
    }
}
