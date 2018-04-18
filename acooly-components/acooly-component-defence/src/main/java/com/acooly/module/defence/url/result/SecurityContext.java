package com.acooly.module.defence.url.result;

/**
 * @author qiubo@yiji.com
 */
public class SecurityContext {
    private static final ThreadLocal<SecurityContext> LOCAL = ThreadLocal.withInitial(() -> new SecurityContext());
    private String[] fields;

    public static SecurityContext getContext() {
        return LOCAL.get();
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
