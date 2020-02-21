package com.acooly.module.tenant.event;

import com.acooly.module.tenant.core.TenantContext;
import com.acooly.module.tenant.core.TenantMessage;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.bus.MessagePublication;
import net.engio.mbassy.dispatch.IHandlerInvocation;
import net.engio.mbassy.dispatch.MessageDispatcher;
import net.engio.mbassy.subscription.SubscriptionContext;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class ExMessageDispatcherX extends MessageDispatcher {
    private final IHandlerInvocation invocation;

    public ExMessageDispatcherX(SubscriptionContext context, IHandlerInvocation invocation) {
        super(context, invocation);
        this.invocation = invocation;
    }

    @Override
    public void dispatch(
            final MessagePublication publication, final Object message, final Iterable listeners) {
        publication.markDispatched();
        for (Object listener : listeners) {
            log.info("listener:{} 收到消息:{}", listener.getClass().getSimpleName(), message);
            if(message instanceof TenantMessage){
                TenantContext.set(((TenantMessage)message).getTenantId());
            }
            getInvocation().invoke(listener, message, publication);
        }
    }

    @Override
    public IHandlerInvocation getInvocation() {
        return invocation;
    }
}
