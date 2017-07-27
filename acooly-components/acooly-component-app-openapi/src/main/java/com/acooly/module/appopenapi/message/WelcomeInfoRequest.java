/** create by zhangpu date:2015年5月6日 */
package com.acooly.module.appopenapi.message;

import com.acooly.module.appopenapi.enums.DeviceType;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiRequest;

/**
 * 欢迎信息
 *
 * @author zhangpu
 */
@OpenApiMessage(service = "welcomeInfo", type = ApiMessageType.Request)
public class WelcomeInfoRequest extends ApiRequest {

  @OpenApiField(desc = "设备类型", constraint = "如果不传，则返回默认规格图片")
  private DeviceType deviceType;

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(DeviceType deviceType) {
    this.deviceType = deviceType;
  }
}
