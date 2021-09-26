/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-10-28 00:33
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.system.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2020-10-28 00:33
 */
@Slf4j
public class IPUtilTest {

    @Test
    public void isRange() {
        // 增加IPUtil工具方法isInRange,判断IP是否在指定网段内（子网）。例如：`IPUtil.isInRange("192.168.1.2", "192.168.0.0/24")`
        log.info("in RANGE {}", IPUtil.isInRange("192.168.1.127", "192.168.1.64/26"));
        System.out.println(IPUtil.isInRange("192.168.1.2", "192.168.0.0/24"));
        System.out.println(IPUtil.isInRange("192.168.0.1", "192.168.0.0/24"));
        System.out.println(IPUtil.isInRange("192.168.0.0", "192.168.0.0/32"));

    }
}
