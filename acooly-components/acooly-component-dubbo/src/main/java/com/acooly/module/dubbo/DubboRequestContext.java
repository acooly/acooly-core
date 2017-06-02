package com.acooly.module.dubbo;

/** @author qiubo@yiji.com */
public class DubboRequestContext {
  /** 获取请求上下文中的partnerId */
  public static String getPartnerId() {
    return RequestContextFilter.RequestContext.getContext().getPartnerId();
  }

  /** 获取请求上下文中的gid */
  public static String getGid() {
    return RequestContextFilter.RequestContext.getContext().getGid();
  }
}
