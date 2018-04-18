package com.acooly.module.event;

import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.bus.MessagePublication;
import net.engio.mbassy.dispatch.IHandlerInvocation;
import net.engio.mbassy.dispatch.MessageDispatcher;
import net.engio.mbassy.subscription.SubscriptionContext;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class ExMessageDispatcher extends MessageDispatcher {
    private final IHandlerInvocation invocation;

    public ExMessageDispatcher(SubscriptionContext context, IHandlerInvocation invocation) {
        super(context, invocation);
        this.invocation = invocation;
    }

    @Override
    public void dispatch(
            final MessagePublication publication, final Object message, final Iterable listeners) {
        publication.markDispatched();
        for (Object listener : listeners) {
            log.info("listener:{} 收到消息:{}", listener.getClass().getSimpleName(), message);
            getInvocation().invoke(listener, message, publication);
        }
    }

    @Override
    public IHandlerInvocation getInvocation() {
        return invocation;
    }
}
