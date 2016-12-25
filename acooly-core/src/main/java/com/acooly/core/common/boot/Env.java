/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-14 22:46 创建
 */
package com.acooly.core.common.boot;

import com.acooly.core.common.exception.AppConfigException;

/**
 * @author qiubo
 */
public enum Env {
	online,
	pre,
	net,
	test,
	dev,
	local,
	stable,
	sdev,
	stest,
	snet,
	pnet,
	itest,
	unknown;
	public static final String ENV_KEY = "spring.profiles.active";
	public static final String ENV_ENV_KEY = "SPRING_PROFILES_ACTIVE";
	private static Env currEnv = null;
	private static String currEnvStr = null;
	
	public static Env getCurrEnv() {
		if (currEnv == null) {
			getEnv();
		}
		return currEnv;
	}
	
	/**
	 * 获取当前系统环境变量
	 *
	 * @return 环境标识
	 */
	public static String getEnv() {
		if (currEnvStr == null) {
			currEnvStr = System.getProperty(ENV_KEY);
			if (currEnvStr == null) {
				currEnvStr = System.getenv(ENV_ENV_KEY);
			}
			if (currEnvStr != null) {
				try {
					currEnv = Env.valueOf(currEnvStr);
				} catch (IllegalArgumentException e) {
					//ignore
				}
			} else {
				throw new AppConfigException("需要配置系统变量spring.profiles.active或者环境变量SPRING_PROFILES_ACTIVE");
			}
		}
		return currEnvStr;
	}
	
	/**
	 * 判断是否是线上环境 如果没有配置系统变量或者环境变量 spring.profiles.active ,此方法会抛出异常.
	 * @return 是否
	 */
	public static boolean isOnline() {
		return is(online);
	}
	
	/**
	 * 判断是否是本地开发环境
	 */
	public static boolean isLocal() {
		return is(local);
	}
	
	/**
	 * 判断是否是开发测试环境
	 */
	public static boolean isDev() {
		return is(dev);
	}
	
	/**
	 * 判断是否是测试环境
	 */
	public static boolean isTest() {
		return is(test);
	}
	
	/**
	 * 判断是否是预发布环境
	 */
	public static boolean isPre() {
		return is(pre);
	}
	
	/**
	 * 判断是否是联调环境
	 */
	public static boolean isNet() {
		return is(net);
	}
	
	/**
	 * 判断是否是稳定联调环境
	 */
	public static boolean isStable() {
		return is(stable);
	}
	
	/**
	 * 判断是否是集成测试环境
	 */
	public static boolean isItest() {
		return is(itest);
	}
	
	/**
	 * 判读是否是某环境
	 *
	 * @param env 环境
	 */
	public static boolean is(Env env) {
		if (env == null) {
			throw new AppConfigException("env不能为null");
		}
		if (currEnv == null) {
			getEnv();
		}
		return env == currEnv;
	}
	
	/**
	 * 判断给定的枚举，是否在列表中
	 *
	 * @param values 列表
	 * @return
	 */
	public boolean isInList(Env... values) {
		for (Env e : values) {
			if (this == e) {
				return true;
			}
		}
		return false;
	}
	
}
