/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-03 00:02 创建
 */
package com.acooly.module.tenant.dubbo;

import static com.acooly.core.common.boot.log.LogAutoConfig.LogProperties.GID_KEY;

import com.acooly.core.common.facade.Orderable;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.MDC;

/**
 * @author qiubo@yiji.com
 */
@Activate(
        group = {Constants.PROVIDER},
        order = Integer.MIN_VALUE
)
public class MDCFilterX implements Filter {

    @Override
    public Result invoke( Invoker<?> invoker, Invocation inv ) throws RpcException {
        Object[] args = inv.getArguments();
        boolean mdcEnable = false;
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof Orderable) {
                    mdcEnable = true;
                    setGid((Orderable) arg);
                    String tid = ((Orderable) arg).getTenantId();
                    if (tid == null) {
                        tid = "";
                    }
                    MDC.put("tid", tid);
                    break;
                }
            }
        }
        Result result;
        try {
            result = invoker.invoke(inv);
        } finally {
            if (mdcEnable) {
                MDC.remove(GID_KEY);
                MDC.remove("tid");
            }
        }
        return result;
    }

    private void setGid( Orderable arg ) {
        String gid = arg.getGid();
        if (gid == null) {
            gid = "";
        }
        MDC.put(GID_KEY, gid);
    }


}
