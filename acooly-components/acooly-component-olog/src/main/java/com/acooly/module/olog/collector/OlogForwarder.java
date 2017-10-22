package com.acooly.module.olog.collector;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.dubbo.DubboFactory;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.validate.Validators;
import com.acooly.module.olog.facade.api.OlogFacade;
import com.acooly.module.olog.facade.dto.OlogDTO;
import com.acooly.module.olog.facade.order.OlogOrder;
import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/** @author qiubo@yiji.com */
@Component
@Slf4j
public class OlogForwarder implements InitializingBean {
  private static int maxBatchSize = 100;
  private OlogFacade ologFacade;
  private BlockingQueue<OlogDTO> blockingQueue;

  public void logger(OlogDTO dto) {
    try {
      Validators.assertJSR303(dto);
      blockingQueue.put(dto);
    } catch (Exception e) {
      // do nothing
      log.warn("处理日志对象失败:", dto, e);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    try {
      ologFacade = Apps.getApplicationContext().getBean(OlogFacade.class);
    } catch (Exception e) {
      if (e instanceof NoSuchBeanDefinitionException) {
        // ref from dubbo
        if (ClassUtils.isPresent(
            "com.acooly.module.dubbo.DubboAutoConfig",
            Apps.getApplicationContext().getClassLoader())) {
            try {
                ologFacade = new DubboOlogFacadeProvider().getOlogFacade();
            } catch (Exception e1) {
                throw new AppConfigException("olog storage dubbo 服务没有找到，请在boss中启用acooly.olog.storage.enable=true",e1);
            }
        }
      }
    }
    Assert.notNull(ologFacade);
    log.info("ologFacade->{}", ologFacade);
    blockingQueue = new LinkedBlockingQueue<>(1000);
    new OlogBatchConsumer(blockingQueue, ologFacade).start();
  }

  private class DubboOlogFacadeProvider {
    public OlogFacade getOlogFacade() {
      DubboFactory dubboFactory = Apps.getApplicationContext().getBean(DubboFactory.class);
      return dubboFactory.getProxy(OlogFacade.class, null, "1.0", 10000);
    }
  }

  @AllArgsConstructor
  private static class OlogBatchConsumer {
    private BlockingQueue<OlogDTO> blockingQueue;
    private OlogFacade ologFacade;

    public void start() {
      ExecutorService executorService =
          Executors.newSingleThreadExecutor(new CustomizableThreadFactory("olog-forwarder-thread"));
      executorService.submit(this::consume);
    }

    private void consume() {
      while (true) {
        List<OlogDTO> orderList = Lists.newArrayListWithCapacity(maxBatchSize);
        try {
          int takeCount = take(blockingQueue, orderList, maxBatchSize, 10, TimeUnit.SECONDS);
          if (takeCount == 0) {
            continue;
          }
          OlogOrder ologOrder = new OlogOrder();
          ologOrder.setGid(Ids.gid());
          ologOrder.setPartnerId("0");
          ologOrder.setList(orderList);
          ologFacade.logger(ologOrder);
          orderList.clear();
        } catch (InterruptedException e) {
          // do nothing
        }
      }
    }

    public static <T> int take(
        BlockingQueue<T> queue,
        Collection<? super T> batch,
        int maxBatchSize,
        long maxLatency,
        TimeUnit maxLatencyUnit)
        throws InterruptedException {
      long maxLatencyNanos = maxLatencyUnit.toNanos(maxLatency);

      int curBatchSize = 0;
      long stopBatchTimeNanos = -1;

      while (true) {
        if (stopBatchTimeNanos == -1) {
          batch.add(queue.take());
          curBatchSize++;
          stopBatchTimeNanos = System.nanoTime() + maxLatencyNanos;
        } else {
          T element = queue.poll(stopBatchTimeNanos - System.nanoTime(), TimeUnit.NANOSECONDS);
          if (element == null) {
            break;
          }
          batch.add(element);
          curBatchSize++;
        }
        curBatchSize += queue.drainTo(batch, maxBatchSize - curBatchSize);

        if (curBatchSize >= maxBatchSize) {
          break;
        }
      }

      return curBatchSize;
    }
  }
}
