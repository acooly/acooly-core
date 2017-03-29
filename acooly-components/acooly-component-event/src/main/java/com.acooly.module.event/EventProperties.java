package com.acooly.module.event;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.module.event.EventProperties.PREFIX;


@ConfigurationProperties(PREFIX)
@Data
public class EventProperties {
	public static final String PREFIX = "acooly.event";
	private boolean enable = true;

}
