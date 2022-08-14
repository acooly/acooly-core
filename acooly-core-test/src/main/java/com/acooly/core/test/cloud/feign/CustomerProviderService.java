/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 18:21
 */
package com.acooly.core.test.cloud.feign;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.test.core.entity.CoderCustomer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangpu
 * @date 2022-08-13 18:21
 */
@FeignClient(name = "acooly-core", contextId = "customer", path = "/cloud")
public interface CustomerProviderService {

    /**
     * 通过ID读取实体
     *
     * @return
     */
    @GetMapping("/service/customer/read")
    JsonEntityResult<CoderCustomer> read(@RequestParam("id") Long id);

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("/service/customer/page")
    JsonListResult<CoderCustomer> page();

    /**
     * 创建客户
     *
     * @param coderCustomer
     * @return
     */
    @GetMapping("/service/customer/create")
    JsonEntityResult<CoderCustomer> create(CoderCustomer coderCustomer);

}
