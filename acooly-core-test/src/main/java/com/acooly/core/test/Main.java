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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author qiubo
 */
@BootApp(sysName = "acooly-test", httpPort = 8081)
public class Main {
	public static void main(String[] args) {
		Apps.setProfileIfNotExists("sdev");
		SpringApplication.run(Main.class, args);
	}
}
