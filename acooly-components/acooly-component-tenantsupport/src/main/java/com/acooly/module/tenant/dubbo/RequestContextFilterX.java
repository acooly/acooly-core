/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 16:14 创建
 */
package com.acooly.module.tenant.dubbo;

import com.acooly.core.common.facade.BizOrderBase;
import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.Orderable;
import com.acooly.module.dubbo.RequestContext;
import com.acooly.module.tenant.core.TenantContext;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.base.Strings;

/**
 * @author qiubo@yiji.com
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class RequestContextFilterX implements Filter {

    @Override
    public Result invoke( Invoker<?> invoker, Invocation inv ) throws RpcException {
        if (RpcContext.getContext().isProviderSide()) {
            try {
                Object[] args = inv.getArguments();
                if (args != null) {
                    for (Object arg : args) {
                        if (arg instanceof OrderBase) {
                            RequestContext ctx = RequestContext.getContext();
                            if (!Strings.isNullOrEmpty(( (OrderBase) arg ).getTenantId())) {
                                TenantContext.set(( (OrderBase) arg ).getTenantId());
                            }
                            ctx.setGid(( (Orderable) arg ).getGid());
                            ctx.setPartnerId(( (Orderable) arg ).getPartnerId());
                            if (arg instanceof BizOrderBase) {
                                ctx.setBizOrderNo(( (BizOrderBase) arg ).getBizOrderNo());
                                ctx.setMerchOrderNo(( (BizOrderBase) arg ).getMerchOrderNo());
                            }
                            break;
                        }
                    }
                }
                return invoker.invoke(inv);
            } finally {
                RequestContext.removeContext();
                TenantContext.remove();
            }
        } else {
            Object[] args = inv.getArguments();
            if (args != null) {
                for (Object arg : args) {
                    if (arg instanceof OrderBase) {
                        RequestContext ctx = RequestContext.getContext();

                        if (Strings.isNullOrEmpty(( (OrderBase) arg ).getTenantId())) {
                            ( (OrderBase) arg ).setTenantId(TenantContext.get());
                        }

                        if (Strings.isNullOrEmpty(( (OrderBase) arg ).getGid())) {
                            ( (OrderBase) arg ).setGid(ctx.getGid());
                        }
                        if (Strings.isNullOrEmpty(( (OrderBase) arg ).getPartnerId())) {
                            ( (OrderBase) arg ).setPartnerId(ctx.getPartnerId());
                        }
                        if (arg instanceof BizOrderBase) {
                            String bizOrderNo = ( (BizOrderBase) arg ).getBizOrderNo();
                            if (Strings.isNullOrEmpty(bizOrderNo)) {
                                ( (BizOrderBase) arg ).setBizOrderNo(ctx.getBizOrderNo());
                            }
                            String merchOrderNo = ( (BizOrderBase) arg ).getMerchOrderNo();
                            if (Strings.isNullOrEmpty(merchOrderNo)) {
                                ( (BizOrderBase) arg ).setMerchOrderNo(ctx.getMerchOrderNo());
                            }
                        }
                        break;
                    }
                }
            }
            return invoker.invoke(inv);
        }
    }


}
