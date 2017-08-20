
package com.acooly.module.event;

import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.bus.IMessagePublication;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.IBusConfiguration;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.publication.SyncAsyncPostCommand;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

@Slf4j
public class EventBus<T> extends MBassador<T> {

  public EventBus() {}

  public EventBus(IPublicationErrorHandler errorHandler) {
    super(errorHandler);
  }

  public EventBus(IBusConfiguration configuration) {
    super(configuration);
  }

  /** 仅当当前事务提交成功后才发布消息,非事务环境直接发布消息 */
  public void publishAfterTransactionCommitted(T message) {
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
              if (status == TransactionSynchronization.STATUS_COMMITTED) {
                EventBus.this.publish(message);
              }
            }
          });
    } else {
      EventBus.this.publish(message);
    }
  }

  @Override
  public IMessagePublication publishAsync(T message) {
    log.info("发送事件:{}", message);
    return super.publishAsync(message);
  }

  @Override
  public IMessagePublication publishAsync(T message, long timeout, TimeUnit unit) {
    log.info("发送事件:{}", message);
    return super.publishAsync(message, timeout, unit);
  }

  @Override
  public IMessagePublication publish(T message) {
    log.info("发送事件:{}", message);
    return super.publish(message);
  }

  @Override
  public SyncAsyncPostCommand<T> post(T message) {
    log.info("发送事件:{}", message);
    return super.post(message);
  }
}
