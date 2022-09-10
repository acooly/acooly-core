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
 * @author qiubo
 */
//@Configuration
//@EnableConfigurationProperties({AcoolyCloudProperties.class})
//@EnableDiscoveryClient
//@EnableFeignClients
@BootApp(sysName = "acooly-core", httpPort = 8083)
public class Main {
    public static void main(String[] args) {
        // 设置默认参数，全局
//        BigMoney.DEFAULT_SCALE = 4;
//        BigMoney.DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_DOWN;
//        BigMoney.DEFAULT_DB_MODE = BigMoney.DB_MODE_LONG;
//        System.out.println("在Main方法中设置全局静态参数, 测试地址：http://127.0.0.1:8082/manage/test/core/app/testBigMoney.html");
        Apps.setProfileIfNotExists("dev");
        SpringApplication.run(Main.class, args);
    }
}
