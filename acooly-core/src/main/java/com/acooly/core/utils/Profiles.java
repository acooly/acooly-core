/**
 * create by zhangpu
 * date:2016年1月10日
 */
package com.acooly.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 环境操作工具
 * 
 * @author zhangpu
 * @date 2016年1月10日
 */
public class Profiles {

	public enum Profile {
		dev, sdev, test, stest, net, snet, prev, online, other
	}

	public static Profile withOf(String val) {
		for (Profile p : Profile.values()) {
			if (Strings.equals(p.name(), val)) {
				return p;
			}
		}
		return Profile.other;
	}

	public static Profile getProfile() {
		String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		if (Strings.isBlank(activeProfile)) {
			activeProfile = System.getenv(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME);
		}
		if (activeProfile == null) {
			throw Exceptions.runtimeException("需要配置系统或环境变量:" + AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		}
		return withOf(activeProfile);
	}

	public static String getProfileFileName(String fileName) {
		return FilenameUtils.getFullPathNoEndSeparator(fileName) + FilenameUtils.getBaseName(fileName) + "."
		        + getProfile() + "." + FilenameUtils.getExtension(fileName);
	}

	public static void setProfile(Profile profile) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile.name());
	}

	public static boolean isDev() {
		return getProfile() == Profile.dev || getProfile() == Profile.sdev;
	}

	public static boolean isTest() {
		return getProfile() == Profile.test || getProfile() == Profile.stest;
	}

	public static boolean isOnline() {
		return getProfile() == Profile.online;
	}

	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
		System.out.println(getProfileFileName("validation.properties"));
	}
}
