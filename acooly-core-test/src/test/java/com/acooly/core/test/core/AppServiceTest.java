/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-06-30 22:58
 */
package com.acooly.core.test.core;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.test.core.entity.App;
import com.acooly.core.test.core.service.AppService;
import com.acooly.core.utils.BigMoney;
import com.acooly.core.utils.Money;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author zhangpu
 * @date 2021-06-30 22:58
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = com.acooly.core.test.Main.class)
public class AppServiceTest {

    protected static final String PROFILE = "sdev";

    static {
        Apps.setProfileIfNotExists(PROFILE);
    }

    @Autowired
    private AppService appService;

    @Test
    public void testAdd() {
        App app = new App();
        app.setAmount(BigMoney.valueOf("123456789.12345678"));
        app.setName("test");
        app.setDisplayName("测试");
        app.setPrice(Money.amout("1200.55"));
        app.setType("1");
        app.setUserId(1234567l);
        appService.save(app);
        log.info("App Save result: {}", app);
    }

    @Test
    public void testQuery() {
        List<App> apps = appService.query(Maps.newHashMap(), null);
        for (App app : apps) {
            log.info("app_{}: {}", app.getId(), app);
        }
    }

    @Test
    public void testRemoveWithNull() {
        appService.remove(null);
    }

}
