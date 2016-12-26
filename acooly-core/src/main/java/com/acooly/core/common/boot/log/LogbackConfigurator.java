/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-12 21:07 创建
 *
 */
package com.acooly.core.common.boot.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.OptionHelper;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.Env;
import com.google.common.collect.Lists;
import org.springframework.boot.logging.logback.ColorConverter;
import org.springframework.boot.logging.logback.LevelRemappingAppender;
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.acooly.core.common.boot.log.LogAutoConfiguration.LogProperties.pattern;

/**
 * @author qiubo
 */
public class LogbackConfigurator {
	private LoggerContext context;
	private ClassLoader classLoader;
	private Environment environment;
	private String pattern = null;
	private List<String> logs = Lists.newArrayList();
	
	public LogbackConfigurator(LoggerContext context, ClassLoader classLoader, Environment environment) {
		Assert.notNull(context, "Context must not be null");
		this.context = context;
		this.classLoader = classLoader;
		this.environment = environment;
		base();
	}
	
	public LoggerContext getContext() {
		return this.context;
	}
	
	public ClassLoader getClassLoader() {
		return classLoader;
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void conversionRule(String conversionWord, Class<? extends Converter> converterClass) {
		Assert.hasLength(conversionWord, "Conversion word must not be empty");
		Assert.notNull(converterClass, "Converter class must not be null");
		Map<String, String> registry = (Map<String, String>) this.context
			.getObject(CoreConstants.PATTERN_RULE_REGISTRY);
		if (registry == null) {
			registry = new HashMap<>();
			this.context.putObject(CoreConstants.PATTERN_RULE_REGISTRY, registry);
		}
		registry.put(conversionWord, converterClass.getName());
	}
	
	public void appender(String name, Appender<?> appender) {
		appender.setName(name);
		start(appender);
	}
	
	public void logger(String name, Level level) {
		logger(name, level, true);
	}
	
	public void logger(String name, Level level, boolean additive) {
		logger(name, level, additive, null);
	}
	
	public void logger(String name, Level level, boolean additive, Appender<ILoggingEvent> appender) {
		Logger logger = this.context.getLogger(name);
		if (level != null) {
			logger.setLevel(level);
		}
		logger.setAdditive(additive);
		if (appender != null) {
			logger.addAppender(appender);
		}
	}
	
	public void root(Level level, Appender<ILoggingEvent>... appenders) {
		Logger logger = this.context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		if (level != null) {
			logger.setLevel(level);
		}
		for (Appender<ILoggingEvent> appender : appenders) {
			logger.addAppender(appender);
		}
	}
	
	public void start(LifeCycle lifeCycle) {
		if (lifeCycle instanceof ContextAware) {
			((ContextAware) lifeCycle).setContext(this.context);
		}
		lifeCycle.start();
	}
	
	public Logger getRootLogger() {
		return getContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
	}
	
	/**
	 * 创建异步fileAppender 创建的效果如下：
	 * 
	 * <pre>
     *     {@code
     *       <appender class="ch.qos.logback.core.rolling.RollingFileAppender" name="${appenderName}">
     *           <file>${logPath}/${fileName}</file>
     *            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
     *               <fileNamePattern>${logPath}/${fileName}.%d{yyyy-MM-dd}.%i</fileNamePattern>
     *               <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
     *                   <maxFileSize>1024MB</maxFileSize>
     *               </timeBasedFileNamingAndTriggeringPolicy>
     *           </rollingPolicy>
     *           <encoder>
     *               <pattern>${pattern}</pattern>
     *           </encoder>
     *       </appender>
     *         <appender class="com.yjf.common.log.LogbackAsyncAppender" name="async-${appenderName}">
     *                <appender-ref ref="${appenderName}"/>
     *         </appender>
     *}
     * </pre>
	 *
	 * 注意：考虑性能线上环境不会收集栈信息
	 * @param appenderName appender的名字
	 * @param pattern 日志格式
	 * @param fileName 文件名
	 * @param maxHistory 日志文件保留时间
	 * 
	 * @return
	 */
	public Appender<ILoggingEvent> asyncFileAppender(String appenderName, String pattern, String fileName,
														int maxHistory) {
		LogbackAsyncAppender logbackAsyncAppender = new LogbackAsyncAppender();
		logbackAsyncAppender.setContext(context);
		logbackAsyncAppender.setName("ASYNC-" + appenderName);
		RollingFileAppender<ILoggingEvent> appender = fileAppender(appenderName, pattern, fileName, maxHistory);
		
		logbackAsyncAppender.addAppender(appender);
		//线上环境不收集栈信息
		if (Env.isOnline()) {
			logbackAsyncAppender.setIncludeCallerData(false);
		}
		start(logbackAsyncAppender);
		
		return logbackAsyncAppender;
	}
	
	/**
	 * 创建同步fileAppender
	 */
	public RollingFileAppender<ILoggingEvent> fileAppender(String appenderName, String pattern, String fileName) {
		return this.fileAppender(appenderName, pattern, fileName, 0);
	}
	
	/**
	 * 创建同步fileAppender
	 * @param appenderName appender的名字
	 * @param pattern 日志格式
	 * @param fileName 文件名
	 * @param maxHistory 日志文件保留时间
	 * @return
	 */
	public RollingFileAppender<ILoggingEvent> fileAppender(String appenderName, String pattern, String fileName,
															int maxHistory) {
		String logFile = Apps.getLogPath() + fileName;
		RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setPattern(pattern);
		appender.setEncoder(encoder);
		start(encoder);
		
		appender.setFile(logFile);
		
		TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
		rollingPolicy.setFileNamePattern(logFile + ".%d{yyyy-MM-dd}.%i");
		rollingPolicy.setParent(appender);
		rollingPolicy.setMaxHistory(maxHistory);
		rollingPolicy.setCleanHistoryOnStart(true);
		
		SizeAndTimeBasedFNATP<ILoggingEvent> triggeringPolicy = new SizeAndTimeBasedFNATP<>();
		triggeringPolicy.setMaxFileSize("1024MB");
		triggeringPolicy.setTimeBasedRollingPolicy(rollingPolicy);
		rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(triggeringPolicy);
		
		start(rollingPolicy);
		
		appender.setTriggeringPolicy(triggeringPolicy);
		appender.setRollingPolicy(rollingPolicy);
		
		appender(appenderName, appender);
		return appender;
	}
	
	/**
	 * 创建异步fileAppender,
	 * @see LogbackConfigurator#asyncFileAppender(String, String, String, int)
	 */
	public Appender<ILoggingEvent> asyncFileAppender(String appenderName, String pattern, String fileName) {
		return asyncFileAppender(appenderName, pattern, fileName, 0);
	}
	
	/**
	 * base config
	 */
	private void base() {
		//set context name if not set
		setContextNameIfPossible(context);
		// add levelChangePropagator for j.u.l performance tuning
		context.addListener(createLevelChangePropagator());
		//add ansi color converter
		conversionRule("clr", ColorConverter.class);
		conversionRule("wex", WhitespaceThrowableProxyConverter.class);
		// remap logger level info to logger level debug
		LevelRemappingAppender debugRemapAppender = new LevelRemappingAppender("org.springframework.boot");
		start(debugRemapAppender);
		appender("DEBUG_LEVEL_REMAPPER", debugRemapAppender);
		logger("", Level.ERROR);
		logger("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
		logger("org.apache.catalina.util.LifecycleBase", Level.ERROR);
		logger("org.apache.coyote.http11.Http11NioProtocol", Level.WARN);
		logger("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
		logger("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
		logger("org.crsh.plugin", Level.WARN);
		logger("org.crsh.ssh", Level.WARN);
		logger("org.apache.tomcat.util.scan", Level.ERROR);
		logger("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
		logger("org.hibernate.validator.internal.util.Version", Level.WARN);
		logger("org.springframework.boot.actuate.autoconfigure." + "CrshAutoConfiguration", Level.WARN);
		logger("org.springframework.boot.actuate.endpoint.jmx", null, false, debugRemapAppender);
		logger("org.thymeleaf", null, false, debugRemapAppender);
	}
	
	private void setContextNameIfPossible(LoggerContext context) {
		try {
			context.setName(Apps.getAppName());
		} catch (IllegalStateException e) {
			//ignore
		}
	}
	
	/**
	 * ref {@link org.slf4j.bridge.SLF4JBridgeHandler}
	 */
	private LevelChangePropagator createLevelChangePropagator() {
		LevelChangePropagator levelChangePropagator = new LevelChangePropagator();
		levelChangePropagator.setResetJUL(true);
		levelChangePropagator.setContext(this.context);
		levelChangePropagator.start();
		return levelChangePropagator;
	}
	
	/**
	 * 获取日志pattern
	 */
	public String getPattern() {
		if (pattern == null) {
			pattern = OptionHelper.substVars(pattern(), this.getContext());
		}
		return pattern;
	}
	
	/**
	 * 获取日志ansi pattern
	 */
	public String getAnsiPattern() {
		return OptionHelper.substVars(pattern(true), this.getContext());
	}
	
	public void log(String msg, String... param) {
		logs.add(String.format(msg, (Object[]) param));
	}
	
	public List<String> getLogs() {
		return logs;
	}
	
}
