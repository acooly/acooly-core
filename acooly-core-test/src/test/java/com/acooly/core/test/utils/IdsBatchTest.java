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
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.Tasks;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Ids分布式Id单元测试
 *
 * @author zhangpu@acooly.cn
 */
public class IdsBatchTest {

    public static final Logger log = LoggerFactory.getLogger(IdsBatchTest.class);

    final int threads = 200;
    final int timesPerThread = 400;
    final Set<String> container = Collections.synchronizedSet(new HashSet<String>());


    @Test
    public void testDid() {
        String id = Ids.getDid();
        log.info("Did:{},length:{}", id, Strings.length(id));
        id = Ids.gid();
        log.info("gid():{},length:{}", id, Strings.length(id));
        id = Ids.gid("S001");
        log.info("gid(systemCode:S001):{},length:{}", id, Strings.length(id));
        id = Ids.gid("S001", "R12345678");
        log.info("gid(systemCode:S001,reserved:R12345678):{},length:{}",
                id, Strings.length(id));
        id = Ids.oid();
        log.info("oid:{},length:{}", id, Strings.length(id));
        id = Ids.oid("S001");
        log.info("oid(systemCode:S001):{},length:{}", id, Strings.length(id));
        id = Ids.mid();
        log.info("mid():{},length:{}", id, Strings.length(id));

    }

    @Test
    public void testBatchIds() throws Exception {
        AtomicLong successCounter = new AtomicLong();
        log.info("Ids Batch test -> threads:{}, count:{} ", threads, threads * timesPerThread);
        long timeTasks = Tasks.concurrentTasks(threads, new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < timesPerThread; j++) {
                    String id = Ids.gid();
                    if (container.add(id)) {
                        successCounter.incrementAndGet();
                    } else {
                        log.info("重复Id: {}", id);
                    }
                }
            }
        });
        log.info("Ids Batch test -> threads:{}, count:{}, success:{}, ms:{}", threads, threads * timesPerThread, successCounter.longValue(), timeTasks);
        Assert.assertEquals(threads * timesPerThread, successCounter.longValue());
    }
}
