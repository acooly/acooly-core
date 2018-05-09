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
import com.acooly.module.dubbo.mock.DubboLogIgnore;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.*;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qiubo@yiji.com
 */
public class ProviderLogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ProviderLogFilter.class);
    private static final AtomicLong requestId = new AtomicLong();
    private static String providerLogEnableKey = "dubboProviderLogEnableKey";

    private static Map<String, Method> methodMap = Maps.newHashMap();

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {

        String methodString = formatMethodGroup(invoker, inv);

        if (isIgnore(invoker, inv)) {
            return invoker.invoke(inv);
        } else {
            RpcContext context = RpcContext.getContext();
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

    public static boolean isIgnore(Invoker<?> invoker, Invocation inv) {
        Method method = getMethodByCache(invoker, inv);
        if (method == null) {
            return false;
        }
        DubboLogIgnore dubboLogIgnore = method.getAnnotation(DubboLogIgnore.class);
        return dubboLogIgnore != null;
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

    private static Method getMethodByCache(Invoker<?> invoker, Invocation inv) {
        Method method = null;
        String methodFullName = formatMethodGroup(invoker, inv);
        if (methodMap.containsKey(methodFullName)) {
            method = methodMap.get(methodFullName);
        } else {
            Class<?> anInterface = invoker.getInterface();
            String methodName = inv.getMethodName();
            try {
                method = anInterface.getMethod(methodName, inv.getParameterTypes());
                methodMap.put(methodFullName, method);
            } catch (NoSuchMethodException e) {
                logger.error("no such method {}.{}", anInterface.getName(), methodName, e);
            }
        }
        return method;
    }
}
