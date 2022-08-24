/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 18:23
 */
package com.acooly.core.test.cloud;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.PageOrder;
import com.acooly.core.common.facade.PageResult;
import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.test.cloud.feign.AppProviderService;
import com.acooly.core.test.core.entity.App;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpu
 * @date 2022-08-13 18:23
 */
@Slf4j
@RestController
@RequestMapping("/cloud/client/app")
public class AppClientController {

//    @Autowired
    private AppProviderService appProviderService;

    @RequestMapping("get")
    public JsonEntityResult<App> get(@RequestParam("id") Long id) {
        JsonEntityResult<App> result = new JsonEntityResult<>();
        try {
            SingleResult<App> singleResult = appProviderService.get(SingleOrder.from(id));
            result.setEntity(singleResult.getDto());
        } catch (Exception e) {
            result.setCode("REMOTE ERROR");
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping("getFirst")
    public JsonEntityResult<App> getFirst() {
        SingleResult<App> singleResult = appProviderService.getFirst();
        JsonEntityResult<App> result = new JsonEntityResult<>();
        result.setEntity(singleResult.getDto());
        return result;
    }

    @RequestMapping("create")
    public JsonEntityResult<App> create(@RequestBody App app) {
        SingleResult<App> singleResult = appProviderService.create(app);
        JsonEntityResult<App> result = new JsonEntityResult<>();
        result.setEntity(singleResult.getDto());
        return result;
    }

    @RequestMapping("list")
    public JsonListResult<App> list(PageInfo pageInfo) {
        return appProviderService.list(pageInfo);
    }

    @RequestMapping("page")
    public PageResult<App> page(@RequestBody PageOrder pageOrder) {
        return appProviderService.page(pageOrder);
    }

}
