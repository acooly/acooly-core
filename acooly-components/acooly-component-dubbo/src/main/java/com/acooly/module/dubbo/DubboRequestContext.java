package com.acooly.module.dubbo;

/**
 * @author qiubo@yiji.com
 */
public class DubboRequestContext {
    /**
     * 获取请求上下文中的partnerId
     */
    public static String getPartnerId() {
        return RequestContext.getContext().getPartnerId();
    }

    /**
     * 获取请求上下文中的gid
     */
    public static String getGid() {
        return RequestContext.getContext().getGid();
    }

    /**
     * 获取请求上下文中的merchOrderNo
     */
    public static String getMerchOrderNo() {
        return RequestContext.getContext().getMerchOrderNo();
    }

    /**
     * 获取请求上下文中的bizOrderNo
     */
    public static String getBizOrderNo() {
        return RequestContext.getContext().getBizOrderNo();
    }
}
