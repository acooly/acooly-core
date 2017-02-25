/**
 * create by zhangpu
 * date:2016年1月10日
 */
package com.acooly.module.appopenapi.support;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public interface AppApiLoginService {

	String login(String userName, String password, Map<String, Object> context);

}
