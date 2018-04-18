package com.acooly.module.event;

import net.engio.mbassy.dispatch.EnvelopedMessageDispatcher;
import net.engio.mbassy.dispatch.FilteredMessageDispatcher;
import net.engio.mbassy.dispatch.IHandlerInvocation;
import net.engio.mbassy.dispatch.IMessageDispatcher;
import net.engio.mbassy.subscription.SubscriptionContext;
import net.engio.mbassy.subscription.SubscriptionFactory;

/**
 * @author qiubo@yiji.com
 */
public class ExSubscriptionFactory extends SubscriptionFactory {
    protected IMessageDispatcher buildDispatcher(
            SubscriptionContext context, IHandlerInvocation invocation) {
        IMessageDispatcher dispatcher = new ExMessageDispatcher(context, invocation);
        if (context.getHandler().isEnveloped()) {
            dispatcher = new EnvelopedMessageDispatcher(dispatcher);
        }
        if (context.getHandler().isFiltered()) {
            dispatcher = new FilteredMessageDispatcher(dispatcher);
        }
        return dispatcher;
    }
}
