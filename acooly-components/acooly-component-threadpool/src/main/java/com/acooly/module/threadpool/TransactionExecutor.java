package com.acooly.module.threadpool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务执行线程池
 *
 * @author shuijing
 */
public class TransactionExecutor {

  private ThreadPoolTaskExecutor taskExecutor;

  private TransactionTemplate transactionTemplate;

  public TransactionExecutor(
      ThreadPoolTaskExecutor taskExecutor, TransactionTemplate transactionTemplate) {
    this.taskExecutor = taskExecutor;
    this.transactionTemplate = transactionTemplate;
  }

  public void run(final TransactedExecutable transactedExecutable) {
    taskExecutor.execute(
        () ->
            transactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                  @Override
                  protected void doInTransactionWithoutResult(TransactionStatus status) {
                    transactedExecutable.executeTransaction();
                  }
                }));
  }
}
