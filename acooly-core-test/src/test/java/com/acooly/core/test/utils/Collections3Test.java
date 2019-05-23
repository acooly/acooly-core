package com.acooly.core.test.utils;

import com.acooly.core.utils.Collections3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-05-21 19:33
 */
@Slf4j
public class Collections3Test {

    @Test
    public void testTop() {

        List<String> list = Lists.newArrayList("a", "b", "c", "d", "e", "f", "g");
        log.info("collections3.top orignal list: {}", list);
        log.info("collections3.top 3: {}", Collections3.top(list, 3));
        log.info("collections3.top 10: {}", Collections3.top(list, 10));


    }
}
