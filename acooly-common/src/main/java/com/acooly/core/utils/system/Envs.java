package com.acooly.core.utils.system;

import java.lang.management.ManagementFactory;


/**
 * 环境工具
 * 合并到Systems
 * @see com.acooly.core.utils.system.Systems
 */
@Deprecated
public class Envs {

    /**
     * 获取当前进程Id
     *
     * @return 当前进程Id
     */
    public static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }
}
