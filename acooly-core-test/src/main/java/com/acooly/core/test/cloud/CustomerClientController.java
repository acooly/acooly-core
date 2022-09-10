/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 18:23
 */
package com.acooly.core.test.cloud;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.test.cloud.feign.CustomerProviderService;
import com.acooly.core.test.core.entity.CoderCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpu
 * @date 2022-08-13 18:23
 */
@Slf4j
@RestController
@RequestMapping("/cloud/client/customer")
public class CustomerClientController {

//    @Autowired
    private CustomerProviderService customerProviderService;

    @RequestMapping("read")
    public JsonEntityResult<CoderCustomer> read(@RequestParam("id") Long id) {
        return customerProviderService.read(id);
    }

    @RequestMapping("page")
    public JsonListResult<CoderCustomer> page() {
        return customerProviderService.page();
    }

    @RequestMapping("create")
    public JsonEntityResult<CoderCustomer> create(@ModelAttribute("customer") CoderCustomer customer) {
        return customerProviderService.create(customer);
    }
}
