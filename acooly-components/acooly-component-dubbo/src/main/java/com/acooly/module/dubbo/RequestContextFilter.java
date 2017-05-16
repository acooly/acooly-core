/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 16:14 创建
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.Orderable;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.google.common.base.Strings;

/**
 * @author qiubo@yiji.com
 */
@Activate(group = { Constants.PROVIDER, Constants.CONSUMER })
public class RequestContextFilter implements Filter {
	@Override
	public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
		if (RpcContext.getContext().isProviderSide()) {
			try {
				Object[] args = inv.getArguments();
				if (args != null) {
					for (Object arg : args) {
						if (arg instanceof OrderBase) {
							RequestContext requestContext = RequestContext.getContext();
							requestContext.gid = ((Orderable) arg).getGid();
							requestContext.partnerId = ((Orderable) arg).getPartnerId();
							break;
						}
					}
				}
				return invoker.invoke(inv);
			} finally {
				RequestContext.removeContext();
			}
		} else {
			Object[] args = inv.getArguments();
			if (args != null) {
				for (Object arg : args) {
					if (arg instanceof OrderBase) {
						RequestContext requestContext = RequestContext.getContext();
						if (Strings.isNullOrEmpty(((OrderBase) arg).getGid())) {
							((OrderBase) arg).setGid(requestContext.gid);
						}
						if (Strings.isNullOrEmpty(((OrderBase) arg).getPartnerId())) {
							((OrderBase) arg).setPartnerId(requestContext.partnerId);
						}
						break;
					}
				}
			}
			return invoker.invoke(inv);
		}
		
	}
	
	 static class RequestContext {
		private static final ThreadLocal<RequestContext> LOCAL = ThreadLocal.withInitial(() -> new RequestContext());
		
		public static RequestContext getContext() {
			return LOCAL.get();
		}
		
		public static void removeContext() {
			LOCAL.remove();
		}
		
		private String partnerId;
		
		private String gid;

         public String getPartnerId() {
             return partnerId;
         }

         public String getGid() {
             return gid;
         }
     }
}
