/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-12 22:05 创建
 *
 */
package com.acooly.core.common.boot.log.initializer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import com.acooly.core.common.boot.Env;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.SystemUtils;

import java.util.Iterator;

import static com.acooly.core.common.boot.log.LogAutoConfig.LogProperties.consoleEnable;

/**
 * @author qiubo
 */
public class ConsoleLogInitializer extends AbstractLogInitializer {
    //console log默认只启用10分钟
    private static final long CONSOLE_LOG_ENABLE_MILLISECONDS = 60 * 10 * 1000L;

    private static LogbackConfigurator configurator;

    public static void addConsoleAppender() {
        LogbackConfigurator logbackConfigurator = ConsoleLogInitializer.configurator;
        if (logbackConfigurator == null) {
            return;
        }
        logbackConfigurator.log("启用console log");
        //init appender
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(logbackConfigurator.getAnsiPattern());
        encoder.setCharset(Charsets.UTF_8);
        logbackConfigurator.start(encoder);

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setEncoder(encoder);
        logbackConfigurator.appender("CONSOLE", appender);

        removeConsoleAppender(logbackConfigurator);

        logbackConfigurator.root(Level.INFO, appender);
    }

    private static void removeConsoleAppender(LogbackConfigurator logbackConfigurator) {
        Logger logger = logbackConfigurator.getContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

        Iterator<Appender<ILoggingEvent>> iterator = logger.iteratorForAppenders();
        while (iterator.hasNext()) {
            Appender<ILoggingEvent> app = iterator.next();
            if (app instanceof ConsoleAppender) {
                logger.detachAppender(app);
            }
        }
    }

    private static void setConfigurator(LogbackConfigurator configurator) {
        ConsoleLogInitializer.configurator = configurator;
    }

    @Override
    public void init(final LogbackConfigurator configurator) {
        setConfigurator(configurator);
        boolean enable = consoleEnable();
        if (!enable) {
            removeConsoleAppender(configurator);
            return;
        }
        if (Env.isOnline()) {
            addConsoleAppender();
            new Thread(
                    () -> {
                        try {
                            Thread.sleep(CONSOLE_LOG_ENABLE_MILLISECONDS);
                            removeConsoleAppender(configurator);
                        } catch (Exception e) {
                            System.err.println("exception when removing ConsoleAppender: " + e.getMessage());
                        }
                    },
                    "ConsoleAppender-removing-thread")
                    .start();
            return;
        }

        //windows  or mac  enable console log
        if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC || enable) {
            addConsoleAppender();
            return;
        }
    }
}
