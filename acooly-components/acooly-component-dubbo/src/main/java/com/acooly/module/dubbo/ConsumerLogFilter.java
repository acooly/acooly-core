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
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static com.acooly.module.dubbo.ProviderLogFilter.formatMethodGroup;

/**
 * @author qiubo@yiji.com
 */
public class ConsumerLogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerLogFilter.class);

    private static final AtomicLong requestId = new AtomicLong();

    private static Set<String> notNeedLogMethod = Sets.newConcurrentHashSet();

    public static void addIgnoreLogMethod(String methodName) {
        notNeedLogMethod.add(methodName);
    }

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {

        String formatMethod = formatMethodGroup(invoker, inv);

        if (notNeedLogMethod.contains(formatMethod)) {
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
                logger.info("[DUBBO-{}]请求:{}", id, msg);
            } catch (Throwable t) {
                logger.warn(
                        "Exception in ConsumerRequestLogFilter of service(" + invoker + " -> " + inv + ")", t);
            }

            Result result = invoker.invoke(inv);
            logger.info("[DUBBO-{}]响应:{} 耗时:{}ms", id, result, System.currentTimeMillis() - now);
            return result;
        }
    }
}
