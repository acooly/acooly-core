/**
 * create by zhangpu
 * date:2015年12月13日
 */
package com.acooly.module.security.config;

import java.io.Serializable;
import java.util.Map;

/**
 * 安全框架全局配置
 * 
 * @author zhangpu
 * @date 2015年12月13日
 */
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Map<Integer, String> getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Map<Integer, String> userStatus) {
		this.userStatus = userStatus;
	}

	public Map<String, String> getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(Map<String, String> userTypes) {
		this.userTypes = userTypes;
	}

	public boolean isConflict() {
		return conflict;
	}

	public void setConflict(boolean conflict) {
		this.conflict = conflict;
	}

	public boolean isExpire() {
		return expire;
	}

	public void setExpire(boolean expire) {
		this.expire = expire;
	}

	public int getExpireDays() {
		return expireDays;
	}

	public void setExpireDays(int expireDays) {
		this.expireDays = expireDays;
	}

	public boolean isLoginLock() {
		return loginLock;
	}

	public void setLoginLock(boolean loginLock) {
		this.loginLock = loginLock;
	}

	public int getLoginLockErrorTimes() {
		return loginLockErrorTimes;
	}

	public void setLoginLockErrorTimes(int loginLockErrorTimes) {
		this.loginLockErrorTimes = loginLockErrorTimes;
	}

	public long getLoginLockSeconds() {
		return loginLockSeconds;
	}

	public void setLoginLockSeconds(long loginLockSeconds) {
		this.loginLockSeconds = loginLockSeconds;
	}

	public String getPasswordRegex() {
		return passwordRegex;
	}

	public void setPasswordRegex(String passwordRegex) {
		this.passwordRegex = passwordRegex;
	}

	public String getPasswordError() {
		return passwordError;
	}

	public void setPasswordError(String passwordError) {
		this.passwordError = passwordError;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public String toString() {
		return String.format(
				"SecurityConfig: {title:%s, logo:%s, copyright:%s, userStatus:%s, userTypes:%s, conflict:%s, expire:%s, expireDays:%s, loginLock:%s, loginLockErrorTimes:%s, loginLockSeconds:%s, passwordRegex:%s, passwordError:%s}",
				title, logo, copyright, userStatus, userTypes, conflict, expire, expireDays, loginLock,
				loginLockErrorTimes, loginLockSeconds, passwordRegex, passwordError);
	}

}
