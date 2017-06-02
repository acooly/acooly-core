/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-19 22:04 创建
 */
package com.acooly.core.test.openapi;

import com.yiji.framework.openapi.common.enums.ApiBusiType;
import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.core.meta.OpenApiDependence;
import com.yiji.framework.openapi.core.meta.OpenApiService;
import com.yiji.framework.openapi.core.service.base.BaseApiService;
import com.yiji.framework.openapi.core.service.enums.ResponseType;
import lombok.extern.slf4j.Slf4j;

/** @author qiubo@yiji.com */
@OpenApiDependence("createOrder")
@OpenApiService(
  name = "payOrder",
  desc = "支付订单服务",
  responseType = ResponseType.SYN,
  owner = "openApi-arch",
  busiType = ApiBusiType.Trade
)
@Slf4j
public class PayOrderApiService extends BaseApiService<PayOrderRequest, PayOrderResponse> {
  @Override
  protected void doService(PayOrderRequest request, PayOrderResponse response) {
    log.info("success");
    response.setResult(ApiServiceResultCode.PROCESSING);
  }
}
