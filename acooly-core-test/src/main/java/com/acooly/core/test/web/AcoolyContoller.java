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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
public class AcoolyContoller extends AbstractStandardEntityController<App, AppService> {

    @Autowired
    private AppService appService;

    @ResponseBody
    @RequestMapping("jsonResult")
    public JsonResult jsonResult( HttpServletRequest request ) {
        JsonResult result = new JsonResult();
        result.appendData("a", "b");
        return result;
    }


    @ResponseBody
    @RequestMapping("trans")
    public String trans( HttpServletRequest request, HttpServletResponse response, Model model )
            throws Exception {
        // ttt(request, response, model);
        appService.doit(request, response, model);
        return "sss";
    }

    @Transactional(rollbackFor = {Exception.class})
    public void ttt( HttpServletRequest request, HttpServletResponse response, Model model )
            throws Exception {
        App s = new App();
        s.setDisplayName("sss");
        s.setName("sss");
        s.setParentId(12121L);
        s.setType("ssss");
        s.setUserId(32323L);
        s.setParentAppId(121212L);
        s.setPrice(new Money(0));

        getEntityService().save(s);
        doList(request, response, model);

    }

    @ResponseBody
    @RequestMapping("jsonEntityResult")
    public JsonEntityResult<App> jsonEntityResult( HttpServletRequest request ) {
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
