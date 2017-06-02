/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.point.domain.PointTrade;
import com.acooly.module.point.enums.PointTradeType;
import com.acooly.module.point.service.PointTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 积分交易信息 管理控制器
 *
 * @author cuifuqiang Date: 2017-02-03 22:50:14
 */
@Controller
@RequestMapping(value = "/manage/point/pointTrade")
public class PointTradeManagerController
    extends AbstractJQueryEntityController<PointTrade, PointTradeService> {

  @SuppressWarnings("unused")
  @Autowired
  private PointTradeService pointTradeService;

  {
    allowMapping = "*";
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allTradeTypes", PointTradeType.mapping());
  }
}
