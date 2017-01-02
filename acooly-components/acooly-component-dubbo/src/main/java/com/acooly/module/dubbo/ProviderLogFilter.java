/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-01-09 11:11 创建
 */
package com.acooly.module.dubbo;

import com.acooly.core.utils.ToString;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qiubo@yiji.com
 */
public class ProviderLogFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(ProviderLogFilter.class);
	
	private static final AtomicLong requestId = new AtomicLong();
	
	private static String providerLogEnableKey = "dubboProviderLogEnableKey";
	
	public static Boolean isDubboProviderLogEnable() {
		RpcContext context = RpcContext.getContext();
		if (context != null) {
			Object o = context.get(providerLogEnableKey);
			if (o != null) {
				return (Boolean) o;
			}
		}
		return Boolean.FALSE;
	}
	
	public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
		long id = requestId.getAndIncrement();
		long now = System.currentTimeMillis();
		try {
			
			RpcContext context = RpcContext.getContext();
			context.set(providerLogEnableKey, Boolean.TRUE);
			String serviceName = invoker.getInterface().getSimpleName();
			String version = invoker.getUrl().getParameter(Constants.VERSION_KEY);
			String group = invoker.getUrl().getParameter(Constants.GROUP_KEY);
			StringBuilder sn = new StringBuilder(200);
			if (null != group && group.length() > 0) {
				sn.append(group).append("/");
			}
			sn.append(serviceName);
			if (null != version && version.length() > 0) {
				sn.append(":").append(version);
			}
			sn.append("#");
			sn.append(inv.getMethodName());
			sn.append("(");
			Class<?>[] types = inv.getParameterTypes();
			if (types != null && types.length > 0) {
				boolean first = true;
				for (Class<?> type : types) {
					if (first) {
						first = false;
					} else {
						sn.append(",");
					}
					sn.append(type.getSimpleName());
				}
			}
			sn.append(") ");
			Object[] args = inv.getArguments();
			if (args != null && args.length > 0) {
				sn.append(ToString.toString(args));
			}
			sn.append(" ip:").append(context.getRemoteHost());
			String msg = sn.toString();
			logger.info("[DUBBO-{}]请求:{}", id, msg);
		} catch (Exception t) {
			logger.warn("Exception in ProviderLogFilter of service( {} -> {})", invoker, inv, t);
		}
		
		Result result = invoker.invoke(inv);
		logger.info("[DUBBO-{}]响应:{} 耗时:{}ms", id, result, System.currentTimeMillis() - now);
		return result;
	}
	
}
