/** create by zhangpu date:2015年5月6日 */
package com.acooly.module.appopenapi.message;

import com.acooly.module.app.enums.DeviceType;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.message.ApiRequest;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 登录 请求报文
 *
 * @author zhangpu
 */
public class LoginRequest extends ApiRequest {

  @NotEmpty
  @Size(max = 50)
  @OpenApiField(desc = "用户名", constraint = "登录的ID")
  private String username;

  @NotEmpty
  @Size(max = 128)
  @OpenApiField(desc = "密码", security = true)
  private String password;

  @OpenApiField(desc = "客户IP")
  private String customerIp;

  @OpenApiField(desc = "登录渠道", constraint = "注意：与注册渠道一致")
  private String channel;

  @OpenApiField(desc = "设备类型")
  private DeviceType deviceType;

  @Size(max = 64)
  @OpenApiField(desc = "设备型号")
  private String deviceModel;

  @Size(max = 64)
  @OpenApiField(desc = "设备标识")
  private String deviceId;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(DeviceType deviceType) {
    this.deviceType = deviceType;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getCustomerIp() {
    return customerIp;
  }

  public void setCustomerIp(String customerIp) {
    this.customerIp = customerIp;
  }
}
