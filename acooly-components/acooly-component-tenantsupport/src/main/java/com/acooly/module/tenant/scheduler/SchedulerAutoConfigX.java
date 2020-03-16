package com.acooly.module.tenant.scheduler;

import static com.acooly.module.scheduler.SchedulerProperties.PREFIX;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.module.scheduler.SchedulerProperties;
import com.acooly.module.scheduler.job.AutowiringSpringBeanJobFactory;
import com.acooly.module.tenant.ds.TenantDatasource;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import javax.sql.DataSource;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author shuijing
 */
@Configuration
@EnableConfigurationProperties({SchedulerProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.module.scheduler")
public class SchedulerAutoConfigX {

    @Autowired
    private SchedulerProperties schedulerProperties;

    @Bean
    public StandardDatabaseScriptIniter schedulerScriptIniter() {
        return new StandardDatabaseScriptIniter() {
            @Override
            public String getEvaluateTable() {
                return "scheduler_rule";
            }

            @Override
            public String getComponentName() {
                return "scheduler";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("scheduler", "scheduler_urls");
            }
        };
    }

    @Bean
    public JobFactory jobFactory( ApplicationContext applicationContext ) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean( DataSource dataSource, JobFactory jobFactory )
            throws IOException {
        if (!( dataSource instanceof TenantDatasource )) {
            throw new IllegalArgumentException("必须在多在多租户下使用!");
        }
        SchedulerFactoryBeanWrapper wrapper = new SchedulerFactoryBeanWrapper();
        TenantDatasource tenantDatasource = (TenantDatasource) dataSource;
        for (Entry<String, DruidDataSource> entry : tenantDatasource.getTenantDataSourceMap()
                .entrySet()) {
            SchedulerFactoryBean factory = new SchedulerFactoryBean();
            //当配置文件修改后，启动的时候更新triggers
            factory.setOverwriteExistingJobs(true);
            factory.setDataSource(entry.getValue());
            factory.setJobFactory(jobFactory);
            factory.setQuartzProperties(quartzProperties());
            wrapper.register(entry.getKey(),factory);
        }
        return wrapper;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Resource quartzResource =
                ApplicationContextHolder.get().getResource("classpath:META-INF/quartz.properties");
        propertiesFactoryBean.setLocation(quartzResource);
        propertiesFactoryBean.afterPropertiesSet();
        Properties properties = propertiesFactoryBean.getObject();
        properties.setProperty(
                "org.quartz.threadPool.threadCount",
                String.valueOf(schedulerProperties.getThreadCount()));
        properties.setProperty(
                "org.quartz.jobStore.clusterCheckinInterval",
                String.valueOf(schedulerProperties.getClusterCheckinInterval()));
        return properties;
    }
}
