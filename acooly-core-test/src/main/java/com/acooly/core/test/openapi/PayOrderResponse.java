/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-19 22:05 创建
 */
package com.acooly.core.test.openapi;

import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.ApiResponse;

/** @author qiubo@yiji.com */
@OpenApiMessage(service = "payOrder", type = ApiMessageType.Response)
public class PayOrderResponse extends ApiResponse {}
