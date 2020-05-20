/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-02 23:59 创建
 */
package com.acooly.module.dubbo;

import com.acooly.core.utils.ToString;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

import static com.acooly.module.dubbo.ProviderLogFilter.isIgnore;

/**
 * @author qiubo@yiji.com
 * @author zhangpu : 切换日志和修正标准
 */
@Slf4j
public class ConsumerLogFilter implements Filter {

    private static final AtomicLong requestId = new AtomicLong();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {

        if (isIgnore(invoker, inv)) {
            return invoker.invoke(inv);
        } else {
            long id = requestId.getAndIncrement();
            long now = System.currentTimeMillis();
            try {
                RpcContext context = RpcContext.getContext();
                String serviceName = invoker.getInterface().getSimpleName();
                String group = invoker.getUrl().getParameter(Constants.GROUP_KEY);
                StringBuilder sn = new StringBuilder(200);
                if (null != group && group.length() > 0) {
                    sn.append(group).append("/");
                }
                sn.append(serviceName);
                sn.append("#");
                sn.append(inv.getMethodName());
                Object[] args = inv.getArguments();
                if (args != null && args.length > 0) {
                    sn.append(ToString.toString(args));
                }
                sn.append(" ip:")
                        .append(context.getRemoteHost())
                        .append(":")
                        .append(context.getRemotePort());
                String msg = sn.toString();
                log.info("[DUBBO-C-{}] 请求:{}", id, msg);
            } catch (Throwable t) {
                log.warn("Exception in ConsumerRequestLogFilter of service(" + invoker + " -> " + inv + ")", t);
            }

            Result result = invoker.invoke(inv);
            log.info("[DUBBO-C-{}] 响应:{} 耗时:{}ms", id, result, System.currentTimeMillis() - now);
            return result;
        }
    }
}
