package com.acooly.module.scheduler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.module.scheduler.SchedulerProperties.PREFIX;

/**
 * @author shuijing
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class SchedulerProperties {
	public static final String PREFIX = "acooly.scheduler";
	
	public boolean enable;

}
