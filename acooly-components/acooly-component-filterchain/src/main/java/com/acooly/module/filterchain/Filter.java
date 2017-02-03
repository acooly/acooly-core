/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-02-18 17:41 创建
 */
package com.acooly.module.filterchain;

import com.acooly.core.utils.lang.Named;
import org.springframework.core.Ordered;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * 过滤器
 * @author qiubo@yiji.com
 */
@NotThreadSafe
public interface Filter<C extends Context> extends Ordered, Named {
	
	/**
	 * 执行过滤方法
	 * @param context 上下文
	 * @param filterChain 过滤器链
	 */
	void doFilter(C context, FilterChain<C> filterChain);
	
	/**
	 * 过滤器执行顺序
	 */
	@Override
	default int getOrder() {
		return 0;
	}
	
	/**
	 * 过滤器名字
	 */
	@Override
	default String getName() {
		return this.getClass().getSimpleName();
	}
}
