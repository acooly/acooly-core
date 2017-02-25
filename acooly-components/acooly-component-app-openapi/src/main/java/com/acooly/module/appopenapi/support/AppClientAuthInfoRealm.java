package com.acooly.module.appopenapi.support;

import com.acooly.module.app.domain.AppCustomer;
import com.acooly.module.app.enums.EntityStatus;
import com.acooly.module.app.service.AppCustomerService;
import com.acooly.module.appopenapi.AppOpenapiProperties;
import com.google.common.collect.Lists;
import com.yiji.framework.openapi.core.auth.realm.impl.CacheableAuthInfoRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.yiji.framework.openapi.core.auth.realm.AuthInfoRealm.APP_CLIENT_REALM;

/**
 * APP-API认证和授权realm实现
 * 
 * @author zhangpu
 *
 */
@Component(APP_CLIENT_REALM)
public class AppClientAuthInfoRealm extends CacheableAuthInfoRealm {
	
	@Autowired
	private AppCustomerService appCustomerService;
	@Autowired
	private AppOpenapiProperties appOpenapiProperties;
	
	@Override
	public String getSecretKey(String partnerId) {
		if (partnerId.equals(appOpenapiProperties.getAnonymonsAccessKey())) {
			return appOpenapiProperties.getAnonymonsSecretKey();
		} else {
			AppCustomer appCustomer = appCustomerService.loadAppCustomer(partnerId, EntityStatus.Enable);
			return appCustomer == null ? "" : appCustomer.getSecretKey();
		}
		
	}
	
	@Override
	public List<String> getAuthorizedServices(String partnerId) {
		if (partnerId.equals(appOpenapiProperties.getAnonymonsAccessKey())) {
			return appOpenapiProperties.getAnonymonsService();
		} else {
			return Lists.newArrayList("*");
		}
	}
	
}
