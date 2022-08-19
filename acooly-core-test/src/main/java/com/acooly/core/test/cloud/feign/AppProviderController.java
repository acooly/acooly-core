/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-14 16:38
 */
package com.acooly.core.test.cloud.feign;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.PageOrder;
import com.acooly.core.common.facade.PageResult;
import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.test.core.entity.App;
import com.acooly.core.test.core.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpu
 * @date 2022-08-14 16:38
 */
@Slf4j
@RestController
@RequestMapping("/cloud/service/app")
public class AppProviderController {

    @Autowired
    private AppService appService;

    @RequestMapping("getFirst")
    public SingleResult<App> getFirst() {
        App app = appService.get(2L);
        return SingleResult.from(app);
    }

    @RequestMapping("list")
    public JsonListResult<App> list(@SpringQueryMap PageInfo<App> pageInfo) {
        log.info("list pageInfo: {}", pageInfo);
        JsonListResult<App> result = new JsonListResult<>();
        return result;
    }

//    @RequestMapping("page")
//    public PageResult<App> page(@RequestBody PageOrder pageOrder) {
//        log.info("pageOrder: {}", pageOrder);
//        PageInfo<App> pageInfo = appService.query(pageOrder.getPageInfo(), pageOrder.getMap(), pageOrder.getSortMap());
//        return PageResult.from(pageInfo, App.class);
//    }

    @RequestMapping("get")
    public SingleResult<App> get(@RequestBody SingleOrder<Long> order) {
        try {
            // 等待1.5秒，测试ribbon默认超时1秒？？
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        App app = appService.get(order.getDto());
        return SingleResult.from(app);
    }

    @RequestMapping("create")
    public SingleResult<App> create(@RequestBody App app) {
        appService.save(app);
        return SingleResult.from(app);
    }

}
