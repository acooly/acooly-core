/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-28 15:32
 */
package com.acooly.core.test.cloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangpu
 * @date 2022-08-28 15:32
 */
@Data
@ConfigurationProperties(prefix = "acooly.cloud")
public class AcoolyCloudProperties {

    private String profile;

    private String user;
}
