/**
 * spring-cloud-demo
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-07-13 11:29
 */
package com.acooly.core.test.cloud;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2022-07-13 11:29
 */
@Slf4j
//@RefreshScope
@RestController
@RequestMapping("/demo")
public class ConfigDemoController {

    //    @Autowired
    private AcoolyCloudProperties acoolyCloudProperties;


    @RequestMapping("/config")
    public Object config() {
        Map<String, Object> config = Maps.newHashMap();
        config.put("profile", acoolyCloudProperties.getProfile());
        config.put("user", acoolyCloudProperties.getUser());
        return config;
    }

}
