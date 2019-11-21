/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-20 11:14
 */
package com.acooly.core.test.portal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangpu
 * @date 2019-11-20 11:14
 */
@Slf4j
@Controller
@RequestMapping("/test/httpmethod/")
public class SimplePortalTestController {


    @RequestMapping("index")
    @ResponseBody
    public Object index(HttpServletRequest request) {
        return "The request http method: " + request.getMethod();
    }

}
