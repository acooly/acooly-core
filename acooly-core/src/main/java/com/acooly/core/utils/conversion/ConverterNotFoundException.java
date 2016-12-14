/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved.
 */

/*
 * 修订记录：
 * LiXiang 2015年1月22日 下午3:22:58 创建
 */
package com.acooly.core.utils.conversion;

/**
 * 如果没有找到可用的类型转换器抛出该异常。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * @author zhangpu 改写为不依赖冗长的异常继承树，直接继承RuntimeExcetion.
 */
public class ConverterNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5811678321082505224L;

	private Class<?> sourceType;
	private Class<?> targetType;

	/**
	 * 构造一个 ConverterNotFoundException 。
	 * 
	 * @param sourceType
	 *            没有找到的类型转换器的源类型。
	 * @param sourceType
	 *            没有找到的类型转换器的目标类型。
	 */
	public ConverterNotFoundException(Class<?> sourceType, Class<?> targetType) {
		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	/**
	 * 构造一个 ConverterNotFoundException 。
	 * 
	 * @param message
	 *            详细消息。
	 * @param sourceType
	 *            没有找到的类型转换器的源类型。
	 * @param sourceType
	 *            没有找到的类型转换器的目标类型。
	 */
	public ConverterNotFoundException(String message, Class<?> sourceType, Class<?> targetType) {
		super(message);
		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	public Class<?> getSourceType() {
		return (Class<?>) this.sourceType;
	}

	public Class<?> getTargetType() {
		return this.targetType;
	}

	@Override
	public String toString() {
		String message = getLocalizedMessage();
		return getClass().getName() + " [sourceType=" + getSourceType() + ", targetType=" + getTargetType() + "]"
				+ (message == null ? "" : message);
	}

}
