/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 14:43
 */
package com.acooly.core.test.cloud.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpu
 * @date 2022-08-13 14:43
 */
@Slf4j
@RestController
@RequestMapping("/cloud/service/demo1")
public class FeignProviderDemo1Controller {

    @GetMapping("get")
    public String get() {
        return "acooly测试";
    }

}
