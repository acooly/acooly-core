/** create by zhangpu date:2015年9月11日 */
package com.acooly.module.appopenapi.message;

import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.ApiResponse;

/**
 * APP崩溃上报 请求
 *
 * @author zhangpu
 * @date 2015年9月11日
 */
@OpenApiMessage(service = "appCrashReport", type = ApiMessageType.Response)
public class AppCrashReportResponse extends ApiResponse {}
