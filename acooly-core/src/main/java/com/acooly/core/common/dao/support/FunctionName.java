package com.acooly.core.common.dao.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标记存储过程
 * 
 * @author zhangpu
 * @date 2012年6月30日
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionName {
	/**
	 * 存储过程调用名称
	 * 
	 * @return String
	 */
	String name();

	/**
	 * pojo中代表总行数的名称
	 * 
	 * @return String
	 */
	String countName();
}
