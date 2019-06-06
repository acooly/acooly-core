package com.acooly.core.test.utils;

import com.acooly.core.test.utils.bean.MaskSubEntity;
import com.acooly.core.utils.Collections3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author zhangpu
 * @date 2019-05-21 19:33
 */
@Slf4j
public class Collections3Test {

    /**
     * 测试: Collections3.top
     */
    @Test
    public void testTop() {
        // simple type
        List<String> list = Lists.newArrayList("a", "b", "c", "d", "e", "f", "g");
        log.info("collections3.top orignal List<String>: {}", list);
        log.info("collections3.top 3: {}", Collections3.top(list, 3));
        log.info("collections3.top 10: {}", Collections3.top(list, 10));

        // bean type
        Set<MaskSubEntity> maskEntities = Sets.newHashSet();
        for (int i = 0; i < 8; i++) {
            maskEntities.add(new MaskSubEntity());
        }

        log.info("collections3.top orignal Set<bean>: {}", maskEntities);
        log.info("collections3.top() 1: {}", Collections3.top(maskEntities, 0));
        log.info("collections3.top() 1: {}", Collections3.top(maskEntities, 1));
        log.info("collections3.top() 3: {}", Collections3.top(maskEntities, 3));
        log.info("collections3.top() 10: {}", Collections3.top(maskEntities, 10));
    }

}
