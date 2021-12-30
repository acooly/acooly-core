/*
 * acooly.cn Inc.
 * Copyright (c) 2020 All Rights Reserved.
 * create by acooly
 * date:2020-07-03
 */
package com.acooly.core.test.core.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.acooly.core.utils.BigMoney;
import com.acooly.core.utils.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.test.core.entity.App;
import com.acooly.core.test.core.service.AppService;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * app 管理控制器
 *
 * @author acooly
 * @date 2020-07-03 16:28:38
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/test/core/app")
public class AppManagerController extends AbstractJsonEntityController<App, AppService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private AppService appService;

    @RequestMapping("testBigMoney")
    @ResponseBody
    public void testBigMoney() {

        App app = new App();
        app.setName("test");
        app.setDisplayName("测试");
        app.setType("1");
        app.setUserId(1234567L);
        app.setPrice(Money.amout("1200.55"));
        app.setAmount(BigMoney.valueOf("123456789.12345678"));
        app.setBalance(BigMoney.valueOf("1.23456789"));
        appService.save(app);
        log.info("App Saved result: {}", app);

        App appQuery = appService.get(app.getId());
        log.info("App Query result: {}", appQuery);

    }


}
