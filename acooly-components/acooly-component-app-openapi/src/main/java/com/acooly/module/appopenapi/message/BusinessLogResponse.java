package com.acooly.module.appopenapi.message;

import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * @author qiuboboy@qq.com
 * @date 2018-04-25 15:02
 */
@OpenApiMessage(service = "bLog", type = ApiMessageType.Response)
public class BusinessLogResponse extends ApiResponse {
}
