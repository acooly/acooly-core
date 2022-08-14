/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 18:21
 */
package com.acooly.core.test.cloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhangpu
 * @date 2022-08-13 18:21
 */
//@FeignClient(value = "acooly-core")
public interface ProviderApi {
    /**
     * @return
     */
//    @GetMapping("/cloud/service/demo1/get")
    ResponseEntity<String> get();
}
