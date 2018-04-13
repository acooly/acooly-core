package com.acooly.module.appopenapi.support;

import com.acooly.module.app.domain.AppCustomer;
import com.acooly.module.app.enums.EntityStatus;
import com.acooly.module.app.service.AppCustomerService;
import com.acooly.module.appopenapi.AppOpenapiProperties;
import com.acooly.openapi.framework.core.auth.realm.impl.CacheableAuthInfoRealm;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm.APP_CLIENT_REALM;

/**
 * APP-API认证和授权realm实现
 *
 * @author zhangpu
 */
@Component(APP_CLIENT_REALM)
public class AppClientAuthInfoRealm extends CacheableAuthInfoRealm {

  @Autowired private AppCustomerService appCustomerService;
  @Autowired private AppOpenapiProperties appOpenapiProperties;
  protected static final String CLIENT_SUFFIX = "client";

  @Override
  public String getSecretKey(String partnerId) {
    if (partnerId.equals(appOpenapiProperties.getAnonymous().getAccessKey())) {
      return appOpenapiProperties.getAnonymous().getSecretKey();
    } else {
      AppCustomer appCustomer = appCustomerService.loadAppCustomer(partnerId, EntityStatus.Enable);
      if (appCustomer == null) {
        throw new ApiServiceAuthenticationException("app认证用户信息不存在，partnerId=" + partnerId);
      }
      return appCustomer.getSecretKey();
    }
  }

  @Override
  public List<String> getAuthorizedServices(String partnerId) {
    if (partnerId.equals(appOpenapiProperties.getAnonymous().getAccessKey())) {
      return appOpenapiProperties.getAnonymous().getServices();
    } else {
      return Lists.newArrayList("*");
    }
  }

  protected String authorizationKey(String accessKey) {
    return accessKey + AUTHZ_CACHE_KEY_PREFIX + CLIENT_SUFFIX;
  }

  protected String authenticationKey(String accessKey) {
    return accessKey + AUTHZ_CACHE_KEY_PREFIX + CLIENT_SUFFIX;
  }
}
