/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-07 14:29 创建
 */
package com.acooly.core.test.web;

import com.acooly.core.test.dubbo.DemoService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiubo@yiji.com
 */
@RestController
public class DubboController {
	@Reference(version = "1.0")
	private DemoService demoService;
	
	@RequestMapping(value = "/dubbo", method = RequestMethod.GET)
	public String get(String msg) {
		return demoService.echo(msg);
	}
	
}
