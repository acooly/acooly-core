/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 23:54 创建
 */
package com.acooly.core.test;

import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import org.springframework.boot.SpringApplication;

/**
 * @author qiubo@yiji.com
 */
@BootApp(sysName = "acooly-test", httpPort = 8080)
public class Main {
    public static void main(String[] args) {
        Apps.setProfileIfNotExists("sdev");
        SpringApplication.run(Main.class,args);
    }
}
