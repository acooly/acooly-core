/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-06 22:15
 */
package com.acooly.core.test.appservice;

import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.acooly.core.test.core.entity.CoderCustomer;
import com.acooly.core.utils.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangpu
 * @date 2022-08-06 22:15
 */
@Slf4j
@Controller
@RequestMapping(value = "/test/appservice/customer")
public class CoderCustomerRemoteManageController {

    @Autowired
    private CoderCustomerRemoteService coderCustomerRemoteService;

    @RequestMapping(value = "unique")
    @ResponseBody
    public SingleResult<CoderCustomer> unique(HttpServletRequest request, HttpServletResponse response) {
        Long id = Servlets.getLongParameter("id");
        SingleOrder<Long> idOrder = SingleOrder.from(id);
        idOrder.gid("12341234123412341234");
        idOrder.setPartnerId("2312341234");
        SingleResult<CoderCustomer> singleResult = coderCustomerRemoteService.getUniqueOne(idOrder);
        return singleResult;
    }


}
