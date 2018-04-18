/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 16:14 创建
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.facade.BizOrderBase;
import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.Orderable;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiubo@yiji.com
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class RequestContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        if (RpcContext.getContext().isProviderSide()) {
            try {
                Object[] args = inv.getArguments();
                if (args != null) {
                    for (Object arg : args) {
                        if (arg instanceof OrderBase) {
                            RequestContext ctx = RequestContext.getContext();
                            ctx.gid = ((Orderable) arg).getGid();
                            ctx.partnerId = ((Orderable) arg).getPartnerId();
                            if (arg instanceof BizOrderBase) {
                                ctx.bizOrderNo = ((BizOrderBase) arg).getBizOrderNo();
                                ctx.merchOrderNo = ((BizOrderBase) arg).getMerchOrderNo();
                            }
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
                        RequestContext ctx = RequestContext.getContext();
                        if (Strings.isNullOrEmpty(((OrderBase) arg).getGid())) {
                            ((OrderBase) arg).setGid(ctx.gid);
                        }
                        if (Strings.isNullOrEmpty(((OrderBase) arg).getPartnerId())) {
                            ((OrderBase) arg).setPartnerId(ctx.partnerId);
                        }
                        if (arg instanceof BizOrderBase) {
                            String bizOrderNo = ((BizOrderBase) arg).getBizOrderNo();
                            if (Strings.isNullOrEmpty(bizOrderNo)) {
                                ((BizOrderBase) arg).setBizOrderNo(ctx.bizOrderNo);
                            }
                            String merchOrderNo = ((BizOrderBase) arg).getMerchOrderNo();
                            if (Strings.isNullOrEmpty(merchOrderNo)) {
                                ((BizOrderBase) arg).setMerchOrderNo(ctx.merchOrderNo);
                            }
                        }
                        break;
                    }
                }
            }
            return invoker.invoke(inv);
        }
    }

    @Getter
    @Setter
    public static class RequestContext {
        private static final ThreadLocal<RequestContext> LOCAL =
                ThreadLocal.withInitial(() -> new RequestContext());
        private String partnerId;
        private String gid;
        private String merchOrderNo;
        private String bizOrderNo;

        public static RequestContext getContext() {
            return LOCAL.get();
        }

        public static void removeContext() {
            LOCAL.remove();
        }
    }
}
