/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-20 11:14
 */
package com.acooly.core.test.defence;

import com.acooly.module.defence.DefenceProperties;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-11-20 11:14
 */
@Slf4j
@Controller
@RequestMapping("/test/defence/")
public class DefencePortalTestController {

    @Autowired
    private DefenceProperties defenceProperties;

    @RequestMapping("hha")
    @ResponseBody
    public Object hha(HttpServletRequest request) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("HHA-Config", defenceProperties.getHha());
        result.put("Request-Host", request.getRemoteHost());
        result.put("X-Forwarded-Host", request.getHeader("X-Forwarded-Host"));
        return result;
    }


}
