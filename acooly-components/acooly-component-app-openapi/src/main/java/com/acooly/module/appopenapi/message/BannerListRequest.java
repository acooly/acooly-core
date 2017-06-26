/** create by zhangpu date:2015年5月10日 */
package com.acooly.module.appopenapi.message;

import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.AppRequest;

/**
 * 首页 banner 列表
 *
 * @author zhangpu
 */
@OpenApiMessage(service = "bannerList", type = ApiMessageType.Request)
public class BannerListRequest extends AppRequest {}
