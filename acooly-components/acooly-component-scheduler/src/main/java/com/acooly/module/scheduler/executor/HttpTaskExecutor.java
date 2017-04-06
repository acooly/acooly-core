package com.acooly.module.scheduler.executor;

import com.acooly.core.utils.net.Https;
import com.acooly.module.scheduler.domain.SchedulerRule;
import com.acooly.module.scheduler.exceptions.SchedulerExecuteException;

/**
 * @author shuijing
 */
@TaskExecutor.Type(type = TaskTypeEnum.HTTP_TASK)
public class HttpTaskExecutor implements TaskExecutor {

    public static int TIME_OUT = 10 * 1000;
    private Https httpUtil;

    @Override
    public Boolean execute(SchedulerRule schedulerRule) {
        String address = schedulerRule.getProperties();
        try {
            httpUtil.post(address.trim(), null, null, true, "utf-8");
        } catch (Exception e) {
            throw new SchedulerExecuteException(e);
        }
        return true;
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        httpUtil = Https.getInstance();
        httpUtil.readTimeout(TIME_OUT).connectTimeout(TIME_OUT);
    }
}
