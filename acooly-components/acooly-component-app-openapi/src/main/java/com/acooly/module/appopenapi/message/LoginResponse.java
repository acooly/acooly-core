package com.acooly.module.appopenapi.message;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiResponse;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 登录 响应报文
 *
 * @author zhangpu
 */
@OpenApiMessage(service = "login", type = ApiMessageType.Response)
public class LoginResponse extends ApiResponse {

  @NotEmpty
  @Size(min = 8, max = 16)
  @OpenApiField(desc = "访问码", constraint = "客户的用户名,作为登录所有接口的签名accessKey")
  private String accessKey;

  @NotEmpty
  @Size(min = 40, max = 40)
  @OpenApiField(desc = "安全码", constraint = "登录后所有接口的签名秘钥")
  private String secretKey;

  @NotEmpty
  @OpenApiField(desc = "客户id", constraint = "客户id")
  private String customerId;

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
}
