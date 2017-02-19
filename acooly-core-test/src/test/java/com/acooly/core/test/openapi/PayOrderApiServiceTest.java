/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-19 22:11 创建
 */
package com.acooly.core.test.openapi;

import com.acooly.core.utils.Money;
import com.yiji.framework.openapi.core.security.sign.SignTypeEnum;
import com.yiji.framework.openapi.core.test.AbstractApiServieTests;
import org.junit.Test;

import java.util.UUID;

/**
 * @author qiubo@yiji.com
 */
public class PayOrderApiServiceTest extends AbstractApiServieTests {
	{
		gatewayUrl = "http://localhost:8081/gateway.html";
		key = "06aa643ff61bc881318cc41c6a5ff2d5";
		partnerId = "17021922162610100000";
		notifyUrl = "http://127.0.0.1:8081/notify/receiver";
		version = null;
		signType = null;
	}
	
	@Test
	public void testPayOrder() throws Exception {
		service = "payOrder";
		Money amount = Money.amout("1000.00");
		PayOrderRequest request = new PayOrderRequest();
		request.setRequestNo(UUID.randomUUID().toString());
		request.setMerchOrderNo("1234567890=-09876543");
		request.setAmount(amount);
		request.setPayerUserId("09876543211234567890");
		request.setSignType(SignTypeEnum.MD5.toString());
		request.setContext("这是客户端参数:{userName:1,\"password\":\"12121\"}");
		request.setNotifyUrl(notifyUrl);
		request(request, PayOrderResponse.class);
	}
	
}