/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by shuijing
* date:2017-08-01
*/
package com.acooly.module.sms.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.sms.domain.SmsBlackList;
import com.acooly.module.sms.service.BlackListService;
import com.acooly.module.sms.enums.StatusEnum;

/**
 * 短信黑名单 管理控制器
 *
 * @author shuijing
 *         Date: 2017-08-01 17:28:24
 */
@Controller
@RequestMapping(value = "/manage/module/sms/smsBlackList")
public class SmsBlackListManagerController extends AbstractJQueryEntityController<SmsBlackList, BlackListService> {

    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private BlackListService blackListService;


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allStatuss", StatusEnum.mapping());
    }

}
