/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-18 15:08 创建
 */
package com.acooly.core.test.openapi;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.security.Cryptos;
import com.acooly.module.app.enums.DeviceType;
import com.acooly.module.appopenapi.message.BannerListRequest;
import com.acooly.module.appopenapi.message.BannerListResponse;
import com.acooly.module.appopenapi.message.LoginRequest;
import com.acooly.module.appopenapi.message.LoginResponse;
import com.yiji.framework.openapi.core.test.AbstractApiServieTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.UUID;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class AnonymousAppBannerListApiServiceTest extends AbstractApiServieTests {
	{
		gatewayUrl = "http://localhost:8081/gateway.html";
		partnerId = "anonymous";
		key = "anonymouanonymou";
		notifyUrl = null;
		version = null;
		signType = null;
	}
	
	@Test
	public void testBannerList() throws Exception {
		service = "bannerList";
		BannerListRequest request = new BannerListRequest();
		request.setAppClient(true);
		request.setDeviceId("11111111");
		request.setRequestNo(UUID.randomUUID().toString());
		
		BannerListResponse bannerListResponse = request(request, BannerListResponse.class);
		log.info("response:{}", bannerListResponse);
	}
	
	@Test
	public void testLogin() throws Exception {
		service = "login";
		LoginRequest request = new LoginRequest();
		request.setAppClient(true);
		request.setDeviceId("11111111");
		request.setRequestNo(UUID.randomUUID().toString());
		request.setUsername("bohr");
		String password = "bohr";
		request.setPassword(Encodes.encodeBase64(Cryptos.aesEncrypt(password, Encodes.encodeHex(key.getBytes()))));
		request.setDeviceType(DeviceType.IPHONE6);
		request.setDeviceModel("xxxxx");
		LoginResponse response = request(request, LoginResponse.class);
		log.info("response:{}", response);
		
	}
}
