/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-01-03 18:16
 */
package com.acooly.core.test.utils.velocity;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2020-01-03 18:16
 */
@Slf4j
public class VelocitiesTest {

    @Test
    public void testVm() {
        String vmPath = "vm/";
        String vmName = "pname.vm";
        Map paramters = Maps.newHashMap();
        paramters.put("rootArtifactId", "acooly-demo-allinone");
        String result = Velocities.merge(vmName, vmPath, paramters);
        System.out.println(result);
    }

}
