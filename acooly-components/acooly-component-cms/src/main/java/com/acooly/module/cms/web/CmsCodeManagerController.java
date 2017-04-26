/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-04-26
*/
package com.acooly.module.cms.web;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.cms.domain.CmsCode;
import com.acooly.module.cms.service.CmsCodeService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 编码 管理控制器
 *
 * @author acooly
 *         Date: 2017-04-26 17:16:38
 */
@Controller
@RequestMapping(value = "/manage/module/cms/cmsCode")
public class CmsCodeManagerController extends AbstractJQueryEntityController<CmsCode, CmsCodeService> {


    {
        allowMapping = "*";
    }

    @Autowired
    private CmsCodeService cmsCodeService;

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allStatuss", ContentManagerController.allStatuss);


    }

}
