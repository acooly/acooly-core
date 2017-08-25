/*
 * www.acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2016-10-27 23:31 创建
 */
package com.acooly.module.store;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangpu
 */
@ConfigurationProperties(StoreProperties.PREFIX)
@Data
public class StoreProperties {

    public static final String PREFIX = "acooly.store";
    /**
     * 是否启用
     */
    private boolean enable = true;

}
