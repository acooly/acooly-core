/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-06 22:15
 */
package com.acooly.core.test.core.web;

import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.common.enums.ChannelEnum;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.test.core.entity.CoderCustomer;
import com.acooly.core.test.core.service.CoderCustomerService;
import com.acooly.core.test.enums.CustomerTypeEnum;
import com.acooly.core.test.enums.IdcardTypeEnum;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2022-08-06 22:15
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/coder/customer")
public class CoderCustomerManageController extends AbstractStandardEntityController<CoderCustomer, CoderCustomerService> {

    private static Map<Integer, String> allNumStatuss = Maps.newLinkedHashMap();

    static {
        allNumStatuss.put(1, "A");
        allNumStatuss.put(2, "B");
        allNumStatuss.put(3, "C类型");
    }

    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private CoderCustomerService coderCustomerService;


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allGenders", Gender.mapping());
        model.put("allAnimals", AnimalSign.mapping());
        model.put("allIdcardTypes", IdcardTypeEnum.mapping());
        model.put("allCustomerTypes", CustomerTypeEnum.mapping());
        model.put("allRegistryChannels", ChannelEnum.mapping());
        model.put("allPushAdvs", WhetherStatus.mapping());
        model.put("allNumStatuss", allNumStatuss);
        model.put("allStatuss", SimpleStatus.mapping());
    }

}
