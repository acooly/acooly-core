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
import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.test.appservice.CoderCustomerRemoteService;
import com.acooly.core.test.core.entity.CoderCustomer;
import com.acooly.core.test.core.service.CoderCustomerService;
import com.acooly.core.test.enums.CustomerTypeEnum;
import com.acooly.core.test.enums.IdcardTypeEnum;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2022-08-06 22:15
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/coder/customer")
public class CoderCustomerManageController extends AbstractJsonEntityController<CoderCustomer, CoderCustomerService> {

    private static Map<Integer, String> allNumStatuss = Maps.newLinkedHashMap();

    static {
        allNumStatuss.put(1, "A");
        allNumStatuss.put(2, "B");
        allNumStatuss.put(3, "C类型");
    }

    @SuppressWarnings("unused")
    @Autowired
    private CoderCustomerService coderCustomerService;
    @Autowired
    private CoderCustomerRemoteService coderCustomerRemoteService;

    {
        allowMapping = "*";
    }

    @RequestMapping(value = "unique")
    @ResponseBody
    public SingleResult<CoderCustomer> unique(HttpServletRequest request, HttpServletResponse response) {
        Long id = Servlets.getLongParameter("id");
        SingleResult<CoderCustomer> singleResult = coderCustomerRemoteService.getUniqueOne(SingleOrder.from(id));
        return singleResult;
    }


//
//    @Override
//    protected String getExportFileName(HttpServletRequest request) {
//        return "客户信息";
//    }
//
//    @Override
//    protected List<String> getExportTitles() {
//        return Lists.newArrayList("ID", "用户名", "年龄", "生日", "性别", "手机号码", "详情", "完成度(%)", "付款率(%)", "薪水(元)", "注册渠道", "创建时间", "更新时间");
//    }
//
//    @Override
//    protected List<Object> doExportRow(CoderCustomer entity) {
//        return Lists.newArrayList(entity.getId(),
//                entity.getUsername(), entity.getAge(), entity.getBirthday(), entity.getGender(),
//                entity.getMobileNo(), entity.getContent(), entity.getDoneRatio(), entity.getPayRate(), entity.getSalary(),
//                entity.getRegistryChannel(), entity.getCreateTime(), entity.getUpdateTime());
//    }

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
