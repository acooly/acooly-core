package com.acooly.module.tenant.event;

import com.acooly.module.tenant.core.TenantContext;
import com.acooly.module.tenant.core.TenantMessage;
import com.alibaba.dubbo.common.utils.StringUtils;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.This;
import net.engio.mbassy.bus.MessagePublication;
import org.slf4j.MDC;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class AdviceX {


    @OnMethodEnter
    public static void before( @This MessagePublication publication ) {
        if (null == TenantContext.get()) {
            Object message = publication.getMessage();
            if (message instanceof TenantMessage) {
                if (!StringUtils
                        .isEmpty(( (TenantMessage) message )
                                .getTenantId())) {
                    TenantContext
                            .set(( (TenantMessage) message ).getTenantId());
                }
            }
        }
        MDC.put("tid",TenantContext.get());
    }

}
