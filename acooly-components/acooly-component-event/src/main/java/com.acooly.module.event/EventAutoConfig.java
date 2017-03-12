package com.acooly.module.event;

import com.acooly.core.common.boot.Apps;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.config.IBusConfiguration;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableConfigurationProperties({ EventProperties.class })
@ConditionalOnProperty(value = "acooly.event.enable", matchIfMissing = true)
@Slf4j
public class EventAutoConfig {
	
	@Bean
	public EventBus messageBus(ThreadPoolTaskExecutor asyncEventExecutorService) {
		Feature.AsynchronousHandlerInvocation asynchronousHandlerInvocation = new Feature.AsynchronousHandlerInvocation();
		asynchronousHandlerInvocation.setExecutor(asyncEventExecutorService.getThreadPoolExecutor());
		EventBus bus = new EventBus(new BusConfiguration().addFeature(Feature.SyncPubSub.Default())
			.addFeature(asynchronousHandlerInvocation).addFeature(Feature.AsynchronousMessageDispatch.Default())
			.addPublicationErrorHandler(new IPublicationErrorHandler.ConsoleLogger())
			.setProperty(IBusConfiguration.Properties.BusId, "global bus"));
		return bus;
	}
	
	@Bean
	public ThreadPoolTaskExecutor asyncEventExecutorService(EventProperties properties) {
		ThreadPoolTaskExecutor bean = new ThreadPoolTaskExecutor();
		bean.setCorePoolSize(properties.getThreadMin());
		bean.setMaxPoolSize(properties.getThreadMax());
		bean.setQueueCapacity(properties.getThreadQueue());
		bean.setKeepAliveSeconds(300);
		bean.setThreadNamePrefix("async-event-");
		bean.setWaitForTasksToCompleteOnShutdown(true);
		bean.setAllowCoreThreadTimeOut(true);
		bean.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return bean;
	}
	
	@Configuration
	public static class EventHandlerConfig {
		@Autowired
		private EventBus eventBus;
		
		@PostConstruct
		public void afterPropertiesSet() throws Exception {
			Map<String, Object> beansWithAnnotation = Apps.getApplicationContext()
				.getBeansWithAnnotation(EventHandler.class);
			for (Object o : beansWithAnnotation.values()) {
				eventBus.subscribe(o);
				log.info("注册事件处理器:", o.getClass().getName());
			}
		}
		
		@PreDestroy
		public void destroy() throws Exception {
			Map<String, Object> beansWithAnnotation = Apps.getApplicationContext()
				.getBeansWithAnnotation(EventHandler.class);
			for (Object o : beansWithAnnotation.values()) {
				eventBus.unsubscribe(o);
				log.info("销毁事件处理器:", o.getClass().getName());
			}
		}
	}
	
}
