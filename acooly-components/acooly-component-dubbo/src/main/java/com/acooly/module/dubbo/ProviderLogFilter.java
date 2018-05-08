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
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qiubo@yiji.com
 */
public class ProviderLogFilter implements Filter {

    public static final String NOT_NEED_LOG_METHOD_KEY = "notNeedLogMethodKey";
    private static final Logger logger = LoggerFactory.getLogger(ProviderLogFilter.class);
    private static final AtomicLong requestId = new AtomicLong();
    private static String providerLogEnableKey = "dubboProviderLogEnableKey";

    private static Set<String> notNeedLogMethod = Sets.newConcurrentHashSet();

    public static void addIgnoreLogMethod(String methodName) {
        notNeedLogMethod.add(methodName);
    }

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

    public static void markCurrentMethodNotPrintLog() {
        RpcContext context = RpcContext.getContext();
        notNeedLogMethod.add(context.get(NOT_NEED_LOG_METHOD_KEY).toString());
    }

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {

        String methodString = formatMethodGroup(invoker, inv);

        if (notNeedLogMethod.contains(methodString)) {
            return invoker.invoke(inv);
        } else {
            RpcContext context = RpcContext.getContext();
            context.set(NOT_NEED_LOG_METHOD_KEY, methodString);
            long id = requestId.getAndIncrement();
            long now = System.currentTimeMillis();
            try {
                context.set(providerLogEnableKey, Boolean.TRUE);
                StringBuilder sn = new StringBuilder(200);
                sn.append(methodString);
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


    public static String formatMethodGroup(Invoker<?> invoker, Invocation inv) {
        String serviceName = invoker.getInterface().getName();
        String group = invoker.getUrl().getParameter(Constants.GROUP_KEY);
        String methodName = inv.getMethodName();
        return formatMethodGroup(serviceName, group, methodName);
    }

    public static String formatMethodGroup(String serviceName, String group, String methodName) {
        String methodString;
        if (Strings.isNullOrEmpty(group)) {
            methodString = String.format("%s#%s", serviceName, methodName);
        } else {
            methodString = String.format("%s/%s#%s", serviceName, group, methodName);
        }
        return methodString;
    }
}
