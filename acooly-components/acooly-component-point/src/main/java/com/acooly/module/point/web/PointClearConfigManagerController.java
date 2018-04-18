/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-04-19
 */
package com.acooly.module.point.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.utils.Dates;
import com.acooly.module.point.domain.PointClearConfig;
import com.acooly.module.point.dto.PointTradeDto;
import com.acooly.module.point.enums.PointClearConfigStatus;
import com.acooly.module.point.service.PointClearConfigService;
import com.acooly.module.point.service.PointTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 积分清零设置 管理控制器
 *
 * @author acooly Date: 2017-04-19 16:24:31
 */
@Controller
@RequestMapping(value = "/manage/point/pointClearConfig")
public class PointClearConfigManagerController
        extends AbstractJQueryEntityController<PointClearConfig, PointClearConfigService> {

    @SuppressWarnings("unused")
    @Autowired
    private PointClearConfigService pointClearConfigService;
    @Autowired
    private PointTradeService pointTradeService;

    {
        allowMapping = "*";
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allStatuss", PointClearConfigStatus.mapping());
    }

    @RequestMapping(value = "pointClear")
    @ResponseBody
    public JsonEntityResult<PointClearConfig> clearJson(
            HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<PointClearConfig> result = new JsonEntityResult<PointClearConfig>();
        allow(request, response, MappingMethod.create);
        try {
            String id = request.getParameter("id");
            PointClearConfig pointClearConfig = pointClearConfigService.get(Long.parseLong(id));
            String startTime =
                    Dates.format(pointClearConfig.getStartClearTime(), Dates.CHINESE_DATETIME_FORMAT_LINE);
            String endTime =
                    Dates.format(pointClearConfig.getEndClearTime(), Dates.CHINESE_DATETIME_FORMAT_LINE);
            String memo = pointClearConfig.getMemo();
            pointClearConfig.setStatus(PointClearConfigStatus.finish);
            pointClearConfigService.update(pointClearConfig);

            PointTradeDto pointTradeDto = new PointTradeDto();
            pointTradeDto.setBusiId("0");
            pointTradeDto.setBusiType("pointClear");
            pointTradeDto.setBusiTypeText("清分清零");
            pointTradeDto.setBusiData(memo);
            pointTradeDto.setMemo(memo);

            pointTradeService.pointClearThread(startTime, endTime, pointTradeDto);
            result.setMessage("积分清零正在处理中,之后刷新查看结果");
        } catch (Exception e) {
            handleException(result, "新增", e);
        }
        return result;
    }
}
