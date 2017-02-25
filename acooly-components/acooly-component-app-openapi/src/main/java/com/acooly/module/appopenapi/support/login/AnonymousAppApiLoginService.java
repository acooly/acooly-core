/**
 * create by zhangpu
 * date:2016年1月10日
 */
package com.acooly.module.appopenapi.support.login;


import com.acooly.module.appopenapi.support.AppApiLoginService;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public class AnonymousAppApiLoginService implements AppApiLoginService {

	@Override
	public String login(String userName, String password, Map<String, Object> context) {
		return userName;
	}
	
	
}
