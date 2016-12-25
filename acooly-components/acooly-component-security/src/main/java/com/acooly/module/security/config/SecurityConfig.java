/**
 * create by zhangpu
 * date:2015年12月13日
 */
package com.acooly.module.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Map;

/**
 * 安全框架全局配置
 * 
 * @author zhangpu
 * @date 2015年12月13日
 */
@ConfigurationProperties("framework")
@Data
public class SecurityConfig implements Serializable {

	private String title = "Acooly sys 3.x";

	private String subtitle;

	private String logo;

	private String copyright = "acooly";

	/** 用户状态 */
	private Map<Integer, String> userStatus;

	/** 用户类型 */
	private Map<String, String> userTypes;

	/** 是否开启同名用户登录互斥 开关 [未实现] */
	private boolean conflict = false;

	/** 是否开启密码过期处理 开关 */
	private boolean expire = false;

	/** 多长时间密码过期 单位天 */
	private int expireDays;

	/** 密码错误次数锁定 开关 */
	private boolean loginLock = false;
	/** 密码错误几次后触发锁定 */
	private int loginLockErrorTimes;
	/** 密码锁定时长 秒 */
	private long loginLockSeconds;

	/** 密码格式组成规则 */
	private String passwordRegex;
	/** 密码格式错误提示 */
	private String passwordError;

}
