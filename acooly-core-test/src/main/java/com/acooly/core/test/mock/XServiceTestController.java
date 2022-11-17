/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-11-17 15:54
 */
package com.acooly.core.test.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpu
 * @date 2022-11-17 15:54
 */
@Slf4j
@RestController
@RequestMapping("/test/xservice/")
public class XServiceTestController {

    @Autowired
    private XService xService;

    @RequestMapping("echo")
    public Object echo(@RequestParam("body") String body) {
        return xService.echo(body);
    }

}
