package com.acooly.module.security;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.acooly.core.utils.ConfigurableConstants;
import com.acooly.module.security.domain.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 基础安全框架常量
 * 
 * @author zhangpu
 * 
 */
public final class SecurityConstants extends ConfigurableConstants {

	// 静态初始化读入portal.properties中的设置
	static {
		init("spring/acooly/module/security/security.properties");
	}

	public static final String USER_TYPE_MAPPING = getProperty("user.type.mapping", "{\"1\":\"管理员\",\"2\":\"操作员\"}");
	public static final Map<Integer, String> USER_STATUS_MAPPING = Maps.newLinkedHashMap();
	public static final Map<String, String> RESOURCE_TYPES = Maps.newLinkedHashMap();
	/** 基础参数 */
	public static final String FRAMWORK_ICONS = getProperty("framwork.icons", "icon-system");
	public static final List<String> FRAMEWORK_ALL_ICONS = Lists.newArrayList(StringUtils.split(FRAMWORK_ICONS, ","));
	public static final String FRAMWORK_TITLE = getProperty("framwork.title", "Acooly Boss 3.4.x");
	public static final String FRAMWORK_SUBTITLE = getProperty("framwork.subtitle", "专注于业务开发，规范最佳实践，自动代码生成，提高70%效率！");
	public static final String FRAMWORK_COPYRIGHT = getProperty("framwork.copyright",
	        "Copyright © 2016 acooly. All rights reserved");
	public static final String FRAMWORK_LOGO = getProperty("framwork.logo", "/manage/image/logo.png");

	/** 是否开启同时登陆冲突处理，true:表示不允许同时登陆 */
	public static final String USER_LOGIN_CONFLICT = getProperty("user.login.conflict", "false");

	/** 是否开启过期处理 */
	public static final boolean USER_LOGIN_EXPIRE = Boolean.valueOf(getProperty("user.login.expire", "true"));
	public static final int USER_LOGIN_NOTICE_DAYS = Integer.parseInt(getProperty("user.login.expire.days", "5"));
	public static final int USER_LOGIN_EXPIRE_DAYS = Integer.parseInt(getProperty("user.login.expire.days", "90"));

	/** 是否开密码错误锁定功能 */
	public static final boolean USER_LOGIN_LOCK = Boolean.valueOf(getProperty("user.login.lock", "true"));
	public static final int USER_LOGIN_LOCK_ERRORTIMES = Integer
	        .parseInt(getProperty("user.login.lock.errortimes", "5"));
	public static final long USER_LOGIN_LOCK_SECONDS = Long.parseLong(getProperty("user.login.lock.seconds", "1800"));

	/** 密码复杂度 */
	public static final String PASSWORD_REGEX = getProperty("user.password.regex", "[\\\\w]{6,16}");
	public static final String PASSWORD_ERROR = getProperty("user.password.error", "密码由任意字母、数字、下划线组成，长度6-16字节");

	public static final Map<Integer, String> LOAD_MODE_MAPPING = Maps.newLinkedHashMap();
	public static final Map<Integer, String> SHOW_MODE_MAPPING = Maps.newLinkedHashMap();
	public static final Map<Integer, String> SHOW_STATE_MAPPING = Maps.newLinkedHashMap();

	public static final int SHOW_STATE_YES = 0;
	public static final int SHOW_STATE_NO = 1;

	public static final int SHOW_MODE_AJAXLOAD = 1;
	public static final int SHOW_MODE_IFRAME = 2;

	public static final int LOAD_MODE_URL = 1;
	public static final int LOAD_MODE_HTML = 2;

	public static final String RESOURCE_TYPE_URL = "URL";
	public static final String RESOURCE_TYPE_FUNCTION = "FUNCTION";
	public static final String RESOURCE_TYPE_MENU = "MENU";

	static {
		SHOW_STATE_MAPPING.put(SHOW_STATE_YES, "显示");
		SHOW_STATE_MAPPING.put(SHOW_STATE_NO, "隐藏");

		SHOW_MODE_MAPPING.put(SHOW_MODE_AJAXLOAD, "AJAX加载");
		SHOW_MODE_MAPPING.put(SHOW_MODE_IFRAME, "IFrame加载");

		LOAD_MODE_MAPPING.put(LOAD_MODE_URL, "URL");
		LOAD_MODE_MAPPING.put(LOAD_MODE_HTML, "文本");

		USER_STATUS_MAPPING.put(User.STATUS_ENABLE, "正常");
		USER_STATUS_MAPPING.put(User.STATUS_LOCK, "冻结");
		USER_STATUS_MAPPING.put(User.STATUS_EXPIRES, "密码过期");
		USER_STATUS_MAPPING.put(User.STATUS_DISABLE, "禁用");

		RESOURCE_TYPES.put(RESOURCE_TYPE_URL, "URL");
		RESOURCE_TYPES.put(RESOURCE_TYPE_FUNCTION, "FUNCTION");
		RESOURCE_TYPES.put(RESOURCE_TYPE_MENU, "MENU");
	}
}
