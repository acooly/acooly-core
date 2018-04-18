/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-13 17:05 创建
 */
package com.acooly.module.point;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.module.point.PointProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class PointProperties {
    public static final String PREFIX = "acooly.point";
    private boolean enable;
}
