/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 18:21
 */
package com.acooly.core.test.cloud.feign;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.PageOrder;
import com.acooly.core.common.facade.PageResult;
import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.test.core.entity.App;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangpu
 * @date 2022-08-13 18:21
 */
//@FeignClient(name = "acooly-core", contextId = "app", path = "/cloud")
public interface AppProviderService {

    /**
     * top1
     *
     * @return
     */
    @GetMapping("/service/app/getFirst")
    SingleResult<App> getFirst();


    /**
     * 根据ID获取实体
     *
     * @param order
     * @return
     */
    @GetMapping("/service/app/get")
    SingleResult<App> get(@RequestBody SingleOrder<Long> order);


    /**
     * 创建新App
     *
     * @param app
     * @return
     */
    @RequestMapping("/service/app/create")
    SingleResult<App> create(@RequestBody App app);

    @RequestMapping("/service/app/list")
    public JsonListResult<App> list(PageInfo pageInfo);

    @RequestMapping("/service/app/page")
    public PageResult<App> page(@RequestBody PageOrder pageOrder);

}
