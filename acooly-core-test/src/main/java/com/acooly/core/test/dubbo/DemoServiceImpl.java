/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-07 14:27 创建
 */
package com.acooly.core.test.dubbo;

import com.acooly.core.utils.service.SingleValueOrder;
import com.acooly.core.utils.service.SingleValueResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiubo@yiji.com
 */
@Service(version = "1.0")
@Slf4j
public class DemoServiceImpl implements DemoService {
	@Reference(version = "1.0")
	private DemoService1 demoService1;
	
	@Override
	public SingleValueResult<String> echo(SingleValueOrder<String> msg) {
		log.info(RpcContext.getContext().getRemoteHost() + ":" + msg);
		demoService1.echo(SingleValueOrder.from(msg.getDto()));
		return SingleValueResult.from(msg.getDto());
	}
}
