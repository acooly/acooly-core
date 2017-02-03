/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-07 14:29 创建
 */
package com.acooly.core.test.dubbo;

import com.acooly.core.utils.service.SingleValueOrder;
import com.acooly.core.utils.service.SingleValueResult;
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
	public SingleValueResult<String> get(String msg) {
		SingleValueOrder<String> request = new SingleValueOrder<>();
		request.gid().partnerId("test");
		request.setDto(msg);
		return demoService.echo(request);
	}
	
}
