/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-08-04 23:48
 */
package com.acooly.core.common.boot;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import java.io.PrintStream;
import java.util.Set;

/**
 * acooly banner
 *
 * @author zhangpu
 * @date 2019-08-04 23:48
 */
public class AcoolyBanner implements Banner {
    private static Set<String> infos = Sets.newLinkedHashSet();

    public static Set<String> getInfos() {
        return infos;
    }

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        printAppInfo(environment);
    }

    private void printAppInfo(Environment environment) {
        //don't init log system in object create phase
        String appName = environment.getProperty("spring.application.name");
        String port = environment.getProperty("server.port");
        Logger logger = LoggerFactory.getLogger(AcoolyBanner.class);
        logger.info("************************************");
        logger.info(
                "应用[{}]开始启动,profile={},http port={},basePackage={}",
                appName,
                Env.getEnv(),
                port,
                Preconditions.checkNotNull(Apps.getBasePackage()));
        logger.info("************************************");
        if (infos != null) {
            infos.forEach(logger::info);
            infos.clear();
        }
    }

}
