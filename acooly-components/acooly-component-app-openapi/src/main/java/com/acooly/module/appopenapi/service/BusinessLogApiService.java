package com.acooly.module.appopenapi.service;

import com.acooly.core.common.log.BusinessLog;
import com.acooly.module.appopenapi.enums.ApiOwners;
import com.acooly.module.appopenapi.message.BusinessLogRequest;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.service.base.BaseApiService;

/**
 * @author qiuboboy@qq.com
 * @date 2018-04-25 14:37
 */
@OpenApiService(
        name = "bLog",
        desc = "业务日志收集服务",
        responseType = ResponseType.SYN,
        owner = ApiOwners.COMMON,
        busiType = ApiBusiType.Query
)
public class BusinessLogApiService extends BaseApiService<BusinessLogRequest, ApiResponse> {
    @Override
    protected void doService(BusinessLogRequest request, ApiResponse response) {
        request.getContents().forEach(bLog -> {
            BusinessLog.log(bLog.getName(), bLog.getBody());
        });
    }
}
