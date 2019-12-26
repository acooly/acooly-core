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
import com.acooly.core.utils.Ids;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author qiubo
 */
@BootApp(sysName = "core-test", httpPort = 8082)
@EnableTransactionManagement(proxyTargetClass = true)
public class Main {
    public static void main(String[] args) {
       // System.out.println(Ids.getDid(20).length());
        Apps.setProfileIfNotExists("online");
        SpringApplication.run(Main.class, args);
    }
}
