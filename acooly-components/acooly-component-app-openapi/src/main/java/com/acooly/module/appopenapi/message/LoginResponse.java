package com.acooly.module.appopenapi.message;

import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.message.ApiResponse;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 登录 响应报文
 * 
 * @author zhangpu
 *
 */
public class LoginResponse extends ApiResponse {

	@NotEmpty
	@Size(min = 8, max = 16)
	@OpenApiField(desc = "访问码", constraint = "客户的用户名,作为登录所有接口的签名accessKey")
	private String accessKey;

	@NotEmpty
	@Size(min = 40, max = 40)
	@OpenApiField(desc = "安全码", constraint = "登录后所有接口的签名秘钥")
	private String secretKey;

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

}
