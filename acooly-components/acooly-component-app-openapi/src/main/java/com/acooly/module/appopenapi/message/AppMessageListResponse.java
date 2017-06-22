/** create by zhangpu date:2016年2月13日 */
package com.acooly.module.appopenapi.message;

import com.acooly.module.appopenapi.dto.AppMessageDto;
import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.PageApiResponse;

/**
 * @author zhangpu
 * @date 2016年2月13日
 */
@OpenApiMessage(service = "appMessageList", type = ApiMessageType.Response)
public class AppMessageListResponse extends PageApiResponse<AppMessageDto> {}
