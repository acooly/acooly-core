/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-08-04 23:48
 */
package com.acooly.core.common.boot;

import com.acooly.core.common.BootApp;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import java.io.PrintStream;
import java.util.List;

/**
 * acooly banner
 *
 * @author zhangpu
 * @date 2019-08-04 23:48
 */
public class AcoolyBanner implements Banner {
    private static List<String> infos = Lists.newArrayList();
    private BootApp application;

    public AcoolyBanner(BootApp application) {
        this.application = application;
    }

    public static List<String> getInfos() {
        return infos;
    }

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        printAppInfo();
    }

    private void printAppInfo() {
        //don't init log system in object create phase
        Logger logger = LoggerFactory.getLogger(AcoolyBanner.class);
        logger.info("************************************");
        logger.info(
                "应用[{}]开始启动,env={},http port={},basePackage={}",
                Apps.getAppName(),
                Env.getEnv(),
                application.httpPort(),
                Preconditions.checkNotNull(Apps.getBasePackage()));
        logger.info("************************************");
        if (infos != null) {
            infos.forEach(logger::info);
            infos.clear();
        }
    }

}
