/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-10-22 00:02
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.system.Systems;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2019-10-22 00:02
 */
@Slf4j
public class SystemsTest {

    @Test
    public void testSystems() {
        Systems.OsPlatform osPlatform = Systems.getOS();
        log.info("OS: {}", osPlatform);
        log.info("OS Info: {}", osPlatform.getOs().toJson());
        log.info("CpuId: {}", Systems.getCpuId());
        log.info("MainboardId: {}", Systems.getMainboardId());
        log.info("DiskId: {}", Systems.getDiskId());
        log.info("Mac: {}", Systems.getMac());
        log.info("SystemId: {}", Systems.getSystemId());
        log.info("HostName: {}", Systems.getHostName());
    }
}
