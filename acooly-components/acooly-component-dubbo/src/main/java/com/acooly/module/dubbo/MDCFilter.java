/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-03 00:02 创建
 */
package com.acooly.module.dubbo;

import com.acooly.core.common.facade.Orderable;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.MDC;

import static com.acooly.core.common.boot.log.LogAutoConfig.LogProperties.GID_KEY;

/** @author qiubo@yiji.com */
@Activate(
  group = {Constants.PROVIDER},
  order = Integer.MIN_VALUE
)
public class MDCFilter implements Filter {

  @Override
  public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
    Object[] args = inv.getArguments();
    boolean mdcEnable = false;
    if (args != null) {
      for (Object arg : args) {
        if (arg instanceof Orderable) {
          mdcEnable = true;
          setGid((Orderable) arg);
          break;
        }
      }
    }
    Result result;
    try {
      result = invoker.invoke(inv);
    } finally {
      if (mdcEnable) {
        MDC.remove(GID_KEY);
      }
    }
    return result;
  }

  private void setGid(Orderable arg) {
    String gid = arg.getGid();
    if (gid == null) {
      gid = "";
    }
    MDC.put(GID_KEY, gid);
  }
}
