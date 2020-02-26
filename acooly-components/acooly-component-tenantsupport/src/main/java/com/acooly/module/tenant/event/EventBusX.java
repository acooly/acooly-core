package com.acooly.module.tenant.event;

import com.acooly.module.tenant.core.TenantContext;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.bus.IMessagePublication;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.IBusConfiguration;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.publication.SyncAsyncPostCommand;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public class EventBusX<T> extends MBassador<T> implements InitializingBean {

    private TransactionTemplate transactionTemplate;

    @Autowired(required = false)
    private PlatformTransactionManager platformTransactionManager;

    public EventBusX() {
    }

    public EventBusX(IPublicationErrorHandler errorHandler) {
        super(errorHandler);
    }

    public EventBusX(IBusConfiguration configuration) {
        super(configuration);
    }

    /**
     * 仅当当前事务提交成功后才发布消息,非事务环境直接发布消息
     */
    public void publishAfterTransactionCommitted(T message) {
        if (TransactionSynchronizationManager.isSynchronizationActive()
                && transactionTemplate != null) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == TransactionSynchronization.STATUS_COMMITTED) {
                                transactionTemplate.execute(
                                        status1 -> {
                                            EventBusX.this.publish(message);
                                            return null;
                                        });
                            }
                        }
                    });
        } else {
            EventBusX.this.publish(message);
        }
    }

    @Override
    public IMessagePublication publishAsync(T message) {
        log.info("发送事件:{}", message);
        message = TenantContext.wrapEvent(message);
        return super.publishAsync(message);
    }

    @Override
    public IMessagePublication publishAsync(T message, long timeout, TimeUnit unit) {
        log.info("发送事件:{}", message);
        message = TenantContext.wrapEvent(message);
        return super.publishAsync(message, timeout, unit);
    }

    @Override
    public IMessagePublication publish(T message) {
        log.info("发送事件:{}", message);
        message = TenantContext.wrapEvent(message);
        return super.publish(message);
    }

    @Override
    public SyncAsyncPostCommand<T> post(T message) {
        log.info("发送事件:{}", message);
        message = TenantContext.wrapEvent(message);
        return super.post(message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (platformTransactionManager != null) {
            DefaultTransactionDefinition defaultTransactionDefinition =
                    new DefaultTransactionDefinition();
            defaultTransactionDefinition.setPropagationBehavior(
                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            transactionTemplate =
                    new TransactionTemplate(platformTransactionManager, defaultTransactionDefinition);
        } else {
            log.warn("spring事务管理器不存在，publishAfterTransactionCommitted方法不会绑定到事务中!");
        }
    }
}
