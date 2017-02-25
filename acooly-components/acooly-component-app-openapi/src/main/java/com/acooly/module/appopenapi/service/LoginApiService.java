package com.acooly.module.appopenapi.service;

import com.acooly.module.app.domain.AppCustomer;
import com.acooly.module.app.enums.EntityStatus;
import com.acooly.module.app.service.AppCustomerService;
import com.acooly.module.appopenapi.AppApiErrorCode;
import com.acooly.module.appopenapi.AppOpenapiProperties;
import com.acooly.module.appopenapi.enums.ApiOwners;
import com.acooly.module.appopenapi.message.LoginRequest;
import com.acooly.module.appopenapi.message.LoginResponse;
import com.acooly.module.appopenapi.support.AppApiLoginService;
import com.google.common.collect.Maps;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.core.meta.OpenApiService;
import com.yiji.framework.openapi.core.service.base.BaseApiService;
import com.yiji.framework.openapi.core.service.enums.ResponseType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;

import java.util.Map;

/**
 * 用户登录
 * 
 * @note
 *       <p>
 *       用户登录服务需要目标项目根据需求实现ApiLoginService接口
 *       </p>
 * @author zhangpu
 *
 */
@OpenApiService(name = "login", desc = "用户登录", responseType = ResponseType.SYN, owner = ApiOwners.COMMON)
public class LoginApiService extends BaseApiService<LoginRequest, LoginResponse> {
    @Autowired
    private AppOpenapiProperties appOpenapiProperties;

	@Autowired
	private AppApiLoginService appApiLoginService;
	@Autowired
	private AppCustomerService appCustomerService;

	@Override
	protected void doService(LoginRequest request, LoginResponse response) {
		try {
			// 登录验证
			Map<String, Object> context = Maps.newHashMap();
			context.put("request", request);
			String accessKey = appApiLoginService.login(request.getUsername(), request.getPassword(), context);
			AppCustomer appCustomer = appCustomerService.loadAppCustomer(accessKey, EntityStatus.Enable);
			// 生成动态安全码
			if (appCustomer == null) {
				appCustomer = new AppCustomer();
				appCustomer.setUserName(request.getUsername());
				appCustomer.setAccessKey(accessKey);
				appCustomer.setDeviceId(request.getDeviceId());
				appCustomer.setDeviceType(request.getDeviceType());
				appCustomer.setDeviceModel(request.getDeviceModel());
				appCustomer = appCustomerService.createAppCustomer(appCustomer);
			} else {
				String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
				if ("online".equals(activeProfile) && appOpenapiProperties.isDeviceIdCheck()
				        && !StringUtils.equals(request.getDeviceId(), appCustomer.getDeviceId())) {
					throw new RuntimeException("设备ID与绑定的设备ID不符");
				}

				if (appOpenapiProperties.isSecretKeyDynamic()) {
					appCustomer = appCustomerService.updateSecretKey(appCustomer);
				}

			}
			response.setAccessKey(appCustomer.getAccessKey());
			response.setSecretKey(appCustomer.getSecretKey());
		} catch (Exception e) {
			throw new ApiServiceException(AppApiErrorCode.LOGIN_FAIL, e.getMessage());
		}
	}
}
