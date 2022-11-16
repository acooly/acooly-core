/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-11-16 23:14
 */
package com.acooly.core.test.appservice;

import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.acooly.core.test.core.entity.CoderCustomer;
import com.acooly.core.test.core.service.CoderCustomerService;
import com.acooly.module.appservice.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangpu
 * @date 2022-11-16 23:14
 */
@Slf4j
@Component
public class CodeCustomerRemoteServiceImpl implements CoderCustomerRemoteService {

    @Autowired
    private CoderCustomerService coderCustomerService;

    @Override
    @AppService
    public SingleResult<CoderCustomer> getUniqueOne(SingleOrder<Long> idOrder) {
        CoderCustomer coderCustomer = coderCustomerService.get(idOrder.getDto());
        return SingleResult.from(coderCustomer);
    }
}
