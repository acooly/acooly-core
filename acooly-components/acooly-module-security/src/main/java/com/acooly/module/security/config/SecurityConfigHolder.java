/**
 * create by zhangpu
 * date:2015年12月13日
 */
package com.acooly.module.security.config;

import java.util.Map;

import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.module.security.SecurityConstants;

/**
 * @author zhangpu
 * @date 2015年12月13日
 */
public class SecurityConfigHolder {

	private static SecurityConfig config = null;

	@SuppressWarnings("unchecked")
	public static SecurityConfig getSecurityConfig() {
		if (config == null) {
			synchronized (SecurityConfigHolder.class) {
				if (config == null) {
					config = new SecurityConfig();
					config.setTitle(SecurityConstants.FRAMWORK_TITLE);
					config.setSubtitle(SecurityConstants.FRAMWORK_SUBTITLE);
					config.setLogo(SecurityConstants.FRAMWORK_LOGO);
					config.setCopyright(SecurityConstants.FRAMWORK_COPYRIGHT);
					config.setConflict(Boolean.valueOf(SecurityConstants.USER_LOGIN_CONFLICT));
					config.setExpire(SecurityConstants.USER_LOGIN_EXPIRE);
					config.setExpireDays(SecurityConstants.USER_LOGIN_EXPIRE_DAYS);
					config.setLoginLock(SecurityConstants.USER_LOGIN_LOCK);
					config.setLoginLockErrorTimes(SecurityConstants.USER_LOGIN_LOCK_ERRORTIMES);
					config.setPasswordError(SecurityConstants.PASSWORD_ERROR);
					config.setPasswordRegex(SecurityConstants.PASSWORD_REGEX);
					config.setUserStatus(SecurityConstants.USER_STATUS_MAPPING);
					config.setUserTypes(JsonMapper.nonDefaultMapper().fromJson(SecurityConstants.USER_TYPE_MAPPING,
							Map.class));
				}
			}
		}
		return config;
	}

}
