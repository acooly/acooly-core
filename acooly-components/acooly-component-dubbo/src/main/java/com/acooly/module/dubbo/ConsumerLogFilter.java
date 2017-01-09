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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qiubo@yiji.com
 */
public class ConsumerLogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerLogFilter.class);

    private static final AtomicLong requestId = new AtomicLong();

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        long id = requestId.getAndIncrement();
        long now = System.currentTimeMillis();
        try {

            RpcContext context = RpcContext.getContext();

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
            sn.append(" 服务器:").append(context.getRemoteHost()).append(":").append(context.getRemotePort());
            String msg = sn.toString();
            logger.info("[DUBBO-{}]请求:{}", id, msg);
        } catch (Throwable t) {
            logger.warn("Exception in ConsumerRequestLogFilter of service(" + invoker + " -> " + inv + ")", t);
        }

        Result result = invoker.invoke(inv);
        logger.info("[DUBBO-{}]响应:{} 耗时:{}ms", id, result, System.currentTimeMillis() - now);
        return result;
    }

}