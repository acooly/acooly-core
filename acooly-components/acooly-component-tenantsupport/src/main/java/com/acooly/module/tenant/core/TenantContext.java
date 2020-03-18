package com.acooly.module.tenant.core;

import com.acooly.core.utils.StringUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * z Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Slf4j
public class TenantContext {


    public static final String TENANT_ID = "tenantId";

    private static Map<String,String> allTenant = new HashMap<>();


    public static Map<String,String> getAllTenantId() {
        return  Collections.unmodifiableMap(allTenant);
    }

    /**
     * 请求来的放入其中
     */
    private static final ThreadLocal<String> tenantThreadLocal = new ThreadLocal<>();

    /**
     *
     */

    public static void set( String tenant ) {
        //强制先删除原来的,防止意外情况
        tenantThreadLocal.remove();
        log.debug("设置租户:{}", tenant);
        tenantThreadLocal.set(tenant);
    }

    /**
     *
     */
    public static String get() {
        String t = tenantThreadLocal.get();
        log.debug("取出租户:{}", t);
        return t;
    }

    /**
     * remove
     */
    public static void remove() {
        tenantThreadLocal.remove();
    }

    /**
     * 包装 {@link Runnable}
     */
    public static Runnable wrap( Runnable r ) {
        final String tenantId = get();
        return () -> {
            try {
                if (!StringUtils.isEmpty(tenantId)) {
                    set(tenantId);
                    MDC.put("tid", tenantId);
                }
                r.run();
            } finally {
                remove();
                MDC.clear();
            }
        };
    }

    /**
     * 包装 {@link Callable}
     */
    public static <T> Callable<T> wrap( Callable<T> c ) {
        final String tenantId = get();
        return () -> {
            try {
                if (!StringUtils.isEmpty(tenantId)) {
                    set(tenantId);
                    MDC.put("tid", tenantId);
                }
                return c.call();
            } finally {
                remove();
                MDC.clear();
            }
        };
    }

    /**
     * 包装 {@link TenantMessage}
     */
    public static <T> T wrapEvent( T t ) {
        if (t instanceof TenantMessage) {
            ( (TenantMessage) t ).setTenantId(get());
        }
        return t;
    }
}
