/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-29 15:40 创建
 */
package com.acooly.core.test.web;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangpu
 */
@RestController
public class TomcatWelcomeFileTestController {

    @RequestMapping("/index")
    public String testFtl(HttpServletRequest request, ModelMap modelMap) {
        return "this is /index";
    }

    @RequestMapping("/sub/index")
    public String testInclude(ModelMap modelMap) {
        return "this is /sub/index";
    }
}
