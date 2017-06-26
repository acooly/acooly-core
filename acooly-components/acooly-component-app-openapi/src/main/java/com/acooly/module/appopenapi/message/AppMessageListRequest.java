/** create by zhangpu date:2016年2月13日 */
package com.acooly.module.appopenapi.message;

import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.PageApiRequest;

/**
 * 推送消息列表 请求
 *
 * @author zhangpu
 * @date 2016年2月13日
 */
@OpenApiMessage(service = "appMessageList", type = ApiMessageType.Request)
public class AppMessageListRequest extends PageApiRequest {}
