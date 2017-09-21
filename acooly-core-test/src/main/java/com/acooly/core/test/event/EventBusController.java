package com.acooly.core.test.event;

import com.acooly.core.test.dao.CityMybatisDao;
import com.acooly.module.event.EventBus;
import com.acooly.module.event.EventHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/** @author qiubo@yiji.com */
@RestController
@EventHandler
@RequestMapping(value = "/event")
@Slf4j
public class EventBusController {
  @Resource private EventBus eventBus;

  @Autowired private CityMybatisDao cityDao;
  @Autowired private SqlSessionFactory sessionFactory;

  @RequestMapping("test")
  @Transactional
  public void test() {
    val holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
    if (holder != null) {
      log.info("{}", holder.getSqlSession().getConnection());
    }
    cityDao.get(1l);
    CreateCustomerEvent event = new CreateCustomerEvent();
    event.setId(1l);
    event.setUserName("dfd");
    eventBus.publishAfterTransactionCommitted(event);
    eventBus.publishAfterTransactionCommitted(event);
    cityDao.get(1l);
  }

  // 同步事件处理器
  @Handler(delivery = Invoke.Synchronously)
  @Transactional
  public void handleCreateCustomerEvent(CreateCustomerEvent event) {
    log.info("{}", event);
    cityDao.get(1l);
    log.info("{}", event);
    // do what you like
  }
}
