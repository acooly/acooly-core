/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-16 14:35 创建
 */
package com.acooly.core.test.web;

import com.acooly.module.sms.ShortMessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiubo@yiji.com
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
	@Autowired
	private ShortMessageSendService shortMessageSendService;
	
	@RequestMapping("sms")
	public void testFtl() {
		shortMessageSendService.send("18580039996", "xxx");
	}
}
