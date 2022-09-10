/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-10 18:13
 */
package com.acooly.core.test;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangpu
 * @date 2022-09-10 18:13
 */
@Slf4j
public class BaiscTest {

    @Test
    public void testCopyOnWrite() {
        List<String> list = Lists.newCopyOnWriteArrayList();
        // init
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        log.info("init: {}", list);

        // addLast
        list.add("z");
        log.info("addLast: {}", list);
        // addFirst
        list.add(0, "a before");
        log.info("addFirst: {}", list);

        // addBefore
        int index = list.indexOf("b");
        list.add(index, "b before");
        log.info("addBefore b: {}", list);

        // addAfter
        int indexAfter = list.indexOf("d") + 1;
        list.add(indexAfter, "d after");
        log.info("addAfter d: {}", list);


    }

}
