package com.acooly.module.scheduler.executor;

import com.acooly.module.dubbo.DubboRemoteProxyFacotry;
import com.acooly.module.scheduler.api.ScheduleCallBackService;
import com.acooly.module.scheduler.domain.SchedulerRule;
import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * dubbo执行器
 *
 * @author shuijing
 */
@TaskExecutor.Type(type = TaskTypeEnum.DUBBO_TASK)
public class DubboTaskExecutor implements TaskExecutor {

  @Autowired private DubboRemoteProxyFacotry dubboRemoteProxyFacotry;

  @Override
  public Boolean execute(SchedulerRule schedulerRule) {
    Map<String, String> map = splitDubboParam(schedulerRule.getDParam());
    RpcContext.getContext().setAttachments(map);

    ScheduleCallBackService scheduleCallBackService =
        dubboRemoteProxyFacotry.getProxy(
            ScheduleCallBackService.class,
            schedulerRule.getDGroup(),
            schedulerRule.getDVersion(),
            TIME_OUT);
    scheduleCallBackService.justDoIT();
    return true;
  }

  @Override
  public void destroy() throws Exception {}

  @Override
  public void afterPropertiesSet() throws Exception {}

  private Map<String, String> splitDubboParam(String dParam) {
    Map<String, String> map = new HashMap<>();
    if (dParam != null) {
      String[] array = dParam.split(",");
      for (String str : array) {
        String[] ret = str.trim().split(":");
        if (ret.length == 2) {
          map.put(ret[0], ret[1]);
        }
      }
    }
    return map;
  }
}
