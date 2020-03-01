package com.acooly.module.tenant.scheduler;

import com.acooly.module.tenant.core.TenantContext;
import com.alibaba.dubbo.common.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class SchedulerFactoryBeanWrapper extends SchedulerFactoryBean implements
        FactoryBean<Scheduler>, ResourceLoaderAware,
        BeanNameAware, ApplicationContextAware, InitializingBean, DisposableBean, SmartLifecycle {

    private Map<String, SchedulerFactoryBean> tenantSchedulerFactoryBean = new HashMap<>();
    private SchedulerWrapper wrapper = new SchedulerWrapper();


    public void register( String tenantId, SchedulerFactoryBean bean ) {
        if (StringUtils.isEmpty(tenantId)) {
            throw new RuntimeException("租户Id不能为空白字符串");
        }
        if (tenantSchedulerFactoryBean.containsValue(tenantId)) {
            throw new RuntimeException("tenantId:" + tenantId + " 已经被注册过了，不能重复注册");
        }
        tenantSchedulerFactoryBean.put(tenantId, bean);
    }


    @Override
    public Scheduler getObject() {
        for (Entry<String, SchedulerFactoryBean> entry : tenantSchedulerFactoryBean.entrySet()) {
            Scheduler scheduler = entry.getValue().getObject();
            try {
                scheduler.getContext().put(TenantContext.TENANT_ID, entry.getKey());
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
            wrapper.registerScheduler(entry.getKey(), scheduler);
        }
        return wrapper;
    }

    @Override
    public Class<? extends Scheduler> getObjectType() {
        return Scheduler.class;
    }

    @Override
    public void setBeanName( String name ) {
        for (Entry<String, SchedulerFactoryBean> entry : tenantSchedulerFactoryBean.entrySet()) {
            entry.getValue().setBeanName(entry.getKey() + "-SchedulerFactoryBean");
        }
    }

    @Override
    public void destroy() throws SchedulerException {
        for (SchedulerFactoryBean schedulerFactoryBean : tenantSchedulerFactoryBean.values()) {
            schedulerFactoryBean.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (SchedulerFactoryBean schedulerFactoryBean : tenantSchedulerFactoryBean.values()) {
            schedulerFactoryBean.afterPropertiesSet();
        }
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext )
            throws BeansException {
        for (SchedulerFactoryBean schedulerFactoryBean : tenantSchedulerFactoryBean.values()) {
            schedulerFactoryBean.setApplicationContext(applicationContext);
        }
    }

    @Override
    public void setResourceLoader( ResourceLoader resourceLoader ) {
        for (SchedulerFactoryBean schedulerFactoryBean : tenantSchedulerFactoryBean.values()) {
            schedulerFactoryBean.setResourceLoader(resourceLoader);
        }
    }

    @Override
    public void start() {
        for (SchedulerFactoryBean schedulerFactoryBean : tenantSchedulerFactoryBean.values()) {
            schedulerFactoryBean.start();
        }
    }

    @Override
    public void stop() {
        for (SchedulerFactoryBean schedulerFactoryBean : tenantSchedulerFactoryBean.values()) {
            schedulerFactoryBean.stop();
        }
    }

    @Override
    public boolean isRunning() {
        boolean flag = true;
        for (SchedulerFactoryBean schedulerFactoryBean : tenantSchedulerFactoryBean.values()) {
            flag = flag && schedulerFactoryBean.isRunning();
        }
        return flag;
    }
}
