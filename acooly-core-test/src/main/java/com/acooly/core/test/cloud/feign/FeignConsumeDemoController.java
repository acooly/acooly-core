/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 18:23
 */
package com.acooly.core.test.cloud.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpu
 * @date 2022-08-13 18:23
 */
@Slf4j
@RestController
@RequestMapping("/cloud/clent")
public class FeignConsumeDemoController {

//    @Autowired
//    private ProviderApi providerApi;

    @GetMapping("test")
    public String test() {
        return "test";
    }

    @GetMapping("get")
    public String get() {
        // 从提供者中获取数据
        ResponseEntity<String> stringResponseEntity = null; //providerApi.get()
        log.info("从提供者中获取数据：{}", stringResponseEntity);
        return stringResponseEntity.getBody();
    }
}
