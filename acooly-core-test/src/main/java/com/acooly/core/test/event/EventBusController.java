package com.acooly.core.test.event;

import com.acooly.module.event.EventBus;
import com.acooly.module.event.EventHandler;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author qiubo@yiji.com */
@RestController
@EventHandler
@RequestMapping(value = "/event")
@Slf4j
public class EventBusController {
  @Autowired private EventBus eventBus;

  @RequestMapping("test")
  public void test() {
    CreateCustomerEvent event = new CreateCustomerEvent();
    event.setId(1l);
    event.setUserName("dfd");
    eventBus.publish(event);
  }

  //同步事件处理器
  @Handler
  public void handleCreateCustomerEvent(CreateCustomerEvent event) {
    log.info("{}", event);
    //do what you like
  }
}
