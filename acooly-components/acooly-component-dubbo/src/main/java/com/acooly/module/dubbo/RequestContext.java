package com.acooly.module.dubbo;

import lombok.Getter;
import lombok.Setter;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Getter
@Setter
public class RequestContext {


    private static final ThreadLocal<RequestContext> LOCAL =
            ThreadLocal.withInitial(() -> new RequestContext());
    private String partnerId;
    private String gid;
    private String merchOrderNo;
    private String bizOrderNo;

    public static RequestContext getContext() {
        return LOCAL.get();
    }

    public static void removeContext() {
        LOCAL.remove();
    }


}
