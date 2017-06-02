/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-07 14:29 创建
 */
package com.acooly.core.test.dubbo;

import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** @author qiubo@yiji.com */
@RestController
public class DubboController {

  @Reference(version = "1.0")
  private DemoFacade demoFacade;

  @RequestMapping(value = "/dubbo", method = RequestMethod.GET)
  public SingleResult<String> get(String msg) {
    SingleOrder<String> request = new SingleOrder<>();
    request.gid().partnerId("test");
    request.setDto(msg);
    return demoFacade.echo(request);
  }
}
