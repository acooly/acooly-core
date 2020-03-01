package com.acooly.module.tenant.scheduler;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.module.tenant.core.TenantContext;
import com.alibaba.dubbo.common.utils.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Slf4j
public class SchedulerWrapper implements Scheduler {


    private Map<String, Scheduler> tenantScheduler = new HashMap<>();

    public void registerScheduler( String tenantId, Scheduler scheduler ) {
        if (StringUtils.isEmpty(tenantId)) {
            throw new RuntimeException("租户Id不能为空白字符串");
        }
        if (tenantScheduler.containsValue(tenantId)) {
            throw new RuntimeException("tenantId:" + tenantId + " 已经被注册过了，不能重复注册");
        }
        tenantScheduler.put(tenantId, scheduler);
    }


    private Scheduler acquireTenantDataSource() {
        String tenant = TenantContext.get();
        if (StringUtils.isEmpty(tenant)) {
            throw new BusinessException("线程上下文中没有租户信息,或者租户ID为空");
        }
        Scheduler scheduler = tenantScheduler.get(tenant);
        if (scheduler == null) {
            throw new BusinessException("找不到租户Id为:" + tenant + " 所对应的调度器");
        }
        log.debug("获取租户:{} 的调度器:{} 成功", tenant, scheduler);
        return scheduler;
    }


    @Override
    public String getSchedulerName() throws SchedulerException {
        return acquireTenantDataSource().getSchedulerName();
    }

    @Override
    public String getSchedulerInstanceId() throws SchedulerException {
        return acquireTenantDataSource().getSchedulerInstanceId();
    }

    @Override
    public SchedulerContext getContext() throws SchedulerException {
        return acquireTenantDataSource().getContext();
    }

    @Override
    public void start() throws SchedulerException {
        acquireTenantDataSource().start();
    }

    @Override
    public void startDelayed( int i ) throws SchedulerException {
        acquireTenantDataSource().startDelayed(i);
    }

    @Override
    public boolean isStarted() throws SchedulerException {
        return acquireTenantDataSource().isStarted();
    }

    @Override
    public void standby() throws SchedulerException {
        acquireTenantDataSource().standby();
    }

    @Override
    public boolean isInStandbyMode() throws SchedulerException {
        return acquireTenantDataSource().isInStandbyMode();
    }

    @Override
    public void shutdown() throws SchedulerException {
        acquireTenantDataSource().shutdown();
    }

    @Override
    public void shutdown( boolean b ) throws SchedulerException {
        acquireTenantDataSource().shutdown(b);
    }

    @Override
    public boolean isShutdown() throws SchedulerException {
        return acquireTenantDataSource().isShutdown();
    }

    @Override
    public SchedulerMetaData getMetaData() throws SchedulerException {
        return acquireTenantDataSource().getMetaData();
    }

    @Override
    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        return acquireTenantDataSource().getCurrentlyExecutingJobs();
    }

    @Override
    public void setJobFactory( JobFactory jobFactory ) throws SchedulerException {
        acquireTenantDataSource().setJobFactory(jobFactory);
    }

    @Override
    public ListenerManager getListenerManager() throws SchedulerException {
        return acquireTenantDataSource().getListenerManager();
    }

    @Override
    public Date scheduleJob( JobDetail jobDetail, Trigger trigger ) throws SchedulerException {
        return acquireTenantDataSource().scheduleJob(jobDetail, trigger);
    }

    @Override
    public Date scheduleJob( Trigger trigger ) throws SchedulerException {
        return acquireTenantDataSource().scheduleJob(trigger);
    }

    @Override
    public void scheduleJobs( Map<JobDetail, Set<? extends Trigger>> map, boolean b )
            throws SchedulerException {
        acquireTenantDataSource().scheduleJobs(map, b);
    }

    @Override
    public void scheduleJob( JobDetail jobDetail, Set<? extends Trigger> set, boolean b )
            throws SchedulerException {
        acquireTenantDataSource().scheduleJob(jobDetail, set, b);
    }

    @Override
    public boolean unscheduleJob( TriggerKey triggerKey ) throws SchedulerException {
        return acquireTenantDataSource().unscheduleJob(triggerKey);
    }

    @Override
    public boolean unscheduleJobs( List<TriggerKey> list ) throws SchedulerException {
        return acquireTenantDataSource().unscheduleJobs(list);
    }

    @Override
    public Date rescheduleJob( TriggerKey triggerKey, Trigger trigger ) throws SchedulerException {
        return acquireTenantDataSource().rescheduleJob(triggerKey, trigger);
    }

    @Override
    public void addJob( JobDetail jobDetail, boolean b ) throws SchedulerException {
        acquireTenantDataSource().addJob(jobDetail, b);
    }

    @Override
    public void addJob( JobDetail jobDetail, boolean b, boolean b1 ) throws SchedulerException {
        acquireTenantDataSource().addJob(jobDetail, b, b1);
    }

    @Override
    public boolean deleteJob( JobKey jobKey ) throws SchedulerException {
        return acquireTenantDataSource().deleteJob(jobKey);
    }

    @Override
    public boolean deleteJobs( List<JobKey> list ) throws SchedulerException {
        return acquireTenantDataSource().deleteJobs(list);
    }

    @Override
    public void triggerJob( JobKey jobKey ) throws SchedulerException {
        acquireTenantDataSource().triggerJob(jobKey);
    }

    @Override
    public void triggerJob( JobKey jobKey, JobDataMap jobDataMap ) throws SchedulerException {
        acquireTenantDataSource().triggerJob(jobKey, jobDataMap);
    }

    @Override
    public void pauseJob( JobKey jobKey ) throws SchedulerException {
        acquireTenantDataSource().pauseJob(jobKey);
    }

    @Override
    public void pauseJobs( GroupMatcher<JobKey> groupMatcher ) throws SchedulerException {
        acquireTenantDataSource().pauseJobs(groupMatcher);
    }

    @Override
    public void pauseTrigger( TriggerKey triggerKey ) throws SchedulerException {
        acquireTenantDataSource().pauseTrigger(triggerKey);
    }

    @Override
    public void pauseTriggers( GroupMatcher<TriggerKey> groupMatcher ) throws SchedulerException {
        acquireTenantDataSource().pauseTriggers(groupMatcher);
    }

    @Override
    public void resumeJob( JobKey jobKey ) throws SchedulerException {
        acquireTenantDataSource().resumeJob(jobKey);
    }

    @Override
    public void resumeJobs( GroupMatcher<JobKey> groupMatcher ) throws SchedulerException {
        acquireTenantDataSource().resumeJobs(groupMatcher);
    }

    @Override
    public void resumeTrigger( TriggerKey triggerKey ) throws SchedulerException {
        acquireTenantDataSource().resumeTrigger(triggerKey);
    }

    @Override
    public void resumeTriggers( GroupMatcher<TriggerKey> groupMatcher ) throws SchedulerException {
        acquireTenantDataSource().resumeTriggers(groupMatcher);
    }

    @Override
    public void pauseAll() throws SchedulerException {
        acquireTenantDataSource().pauseAll();
    }

    @Override
    public void resumeAll() throws SchedulerException {
        acquireTenantDataSource().resumeAll();
    }

    @Override
    public List<String> getJobGroupNames() throws SchedulerException {
        return null;
    }

    @Override
    public Set<JobKey> getJobKeys( GroupMatcher<JobKey> groupMatcher ) throws SchedulerException {
        return acquireTenantDataSource().getJobKeys(groupMatcher);
    }

    @Override
    public List<? extends Trigger> getTriggersOfJob( JobKey jobKey ) throws SchedulerException {
        return acquireTenantDataSource().getTriggersOfJob(jobKey);
    }

    @Override
    public List<String> getTriggerGroupNames() throws SchedulerException {
        return acquireTenantDataSource().getTriggerGroupNames();
    }

    @Override
    public Set<TriggerKey> getTriggerKeys( GroupMatcher<TriggerKey> groupMatcher )
            throws SchedulerException {
        return acquireTenantDataSource().getTriggerKeys(groupMatcher);
    }

    @Override
    public Set<String> getPausedTriggerGroups() throws SchedulerException {
        return acquireTenantDataSource().getPausedTriggerGroups();
    }

    @Override
    public JobDetail getJobDetail( JobKey jobKey ) throws SchedulerException {
        return acquireTenantDataSource().getJobDetail(jobKey);
    }

    @Override
    public Trigger getTrigger( TriggerKey triggerKey ) throws SchedulerException {
        return acquireTenantDataSource().getTrigger(triggerKey);
    }

    @Override
    public TriggerState getTriggerState( TriggerKey triggerKey ) throws SchedulerException {
        return acquireTenantDataSource().getTriggerState(triggerKey);
    }

    @Override
    public void resetTriggerFromErrorState( TriggerKey triggerKey ) throws SchedulerException {
        acquireTenantDataSource().resetTriggerFromErrorState(triggerKey);
    }

    @Override
    public void addCalendar( String s, Calendar calendar, boolean b, boolean b1 )
            throws SchedulerException {
        acquireTenantDataSource().addCalendar(s, calendar, b, b1);
    }

    @Override
    public boolean deleteCalendar( String s ) throws SchedulerException {
        return acquireTenantDataSource().deleteCalendar(s);
    }

    @Override
    public Calendar getCalendar( String s ) throws SchedulerException {
        return acquireTenantDataSource().getCalendar(s);
    }

    @Override
    public List<String> getCalendarNames() throws SchedulerException {
        return acquireTenantDataSource().getCalendarNames();
    }

    @Override
    public boolean interrupt( JobKey jobKey ) throws UnableToInterruptJobException {
        return acquireTenantDataSource().interrupt(jobKey);
    }

    @Override
    public boolean interrupt( String s ) throws UnableToInterruptJobException {
        return acquireTenantDataSource().interrupt(s);
    }

    @Override
    public boolean checkExists( JobKey jobKey ) throws SchedulerException {
        return acquireTenantDataSource().checkExists(jobKey);
    }

    @Override
    public boolean checkExists( TriggerKey triggerKey ) throws SchedulerException {
        return acquireTenantDataSource().checkExists(triggerKey);
    }

    @Override
    public void clear() throws SchedulerException {
        acquireTenantDataSource().clear();
    }
}
