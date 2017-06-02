/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-29 15:40 创建
 */
package com.acooly.core.test.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/** @author qiubo@yiji.com */
@Controller
public class FreemarkerController {

  @RequestMapping("/testFtl")
  public String testFtl(ModelMap modelMap) {
    modelMap.put("name", "na");
    modelMap.put("message", "hi");
    return "test";
  }

  @RequestMapping("/testInclude")
  public String testInclude(ModelMap modelMap) {
    modelMap.put("where", "out");
    return "testInclude";
  }
}
