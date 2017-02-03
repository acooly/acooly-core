/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-09-22 11:09 创建
 */
package com.acooly.module.appservice.filter;


import com.acooly.module.filterchain.Filter;
import com.acooly.module.filterchain.FilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * @author qiubo@yiji.com
 */
public class AppServiceLogFilter implements Filter<AppServiceContext> {
	private static final Logger logger = LoggerFactory.getLogger(AppServiceLogFilter.class);
	
	private boolean dubboNotSupport = false;
	private Class providerLogFilter;
	private Method method;
	
	public AppServiceLogFilter() {
		try {
			providerLogFilter = Class.forName("com.acooly.module.dubbo.ProviderLogFilter");
			method = providerLogFilter.getMethod("isDubboProviderLogEnable");
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			dubboNotSupport = true;
		}
	}
	
	@Override
	public void doFilter(AppServiceContext context, FilterChain<AppServiceContext> filterChain) {
		boolean dubboLogEnable = dubboLogged();
		if (!dubboLogEnable) {
			long begin = System.currentTimeMillis();
			Object[] args = context.getMethodInvocation().getArguments();
			String logMethod = context.getLoggerMethodName();
			logger.info("[{}]请求入参:{}", logMethod, args == null ? "无" : args);
			filterChain.doFilter(context);
			logger.info("[{}]请求响应:{},耗时:{}ms", logMethod, context.getResult() == null ? "无" : context.getResult(),
				System.currentTimeMillis() - begin);
		} else {
			filterChain.doFilter(context);
		}
	}
	
	private boolean dubboLogged() {
		if (dubboNotSupport) {
			return false;
		} else {
			try {
				Boolean logged = (Boolean) method.invoke(providerLogFilter);
				if (logged != null) {
					return logged;
				}
			} catch (Throwable e) {
				//ignore
			}
		}
		return false;
	}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
