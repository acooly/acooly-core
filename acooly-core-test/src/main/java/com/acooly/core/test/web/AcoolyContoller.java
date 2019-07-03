/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-06-23 21:24
 */
package com.acooly.core.test.web;

import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.test.domain.App;
import com.acooly.core.utils.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zhangpu
 * @date 2019-06-23 21:24
 */
@Slf4j
@Controller
@RequestMapping("/test/acooly")
public class AcoolyContoller extends AbstractStandardEntityController {

    @ResponseBody
    @RequestMapping("jsonResult")
    public JsonResult jsonResult(HttpServletRequest request) {
        JsonResult result = new JsonResult();
        result.appendData("a", "b");
        return result;
    }

    @ResponseBody
    @RequestMapping("jsonEntityResult")
    public JsonEntityResult<App> jsonEntityResult(HttpServletRequest request) {
        JsonEntityResult<App> result = new JsonEntityResult();
        App app = new App();
        app.setDisplayName("张浦");
        app.setId(1L);
        app.setParentId(0L);
        app.setPrice(Money.cent(12032));
        app.setRawAddTime(new Date());
        app.setRawUpdateTime(new Date());
        app.setType("test");
        app.setUserId(1212L);
        result.setEntity(app);
        result.appendData("a", "b");
        return result;
    }

}
