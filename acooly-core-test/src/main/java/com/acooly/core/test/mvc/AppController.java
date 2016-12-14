/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-13 15:27 创建
 */
package com.acooly.core.test.mvc;


import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.test.dal.App;
import com.acooly.core.test.service.AppEntityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qiubo@yiji.com
 */
@Controller
@RequestMapping("/app")
public class AppController extends AbstractStandardEntityController<App, AppEntityService> {
}