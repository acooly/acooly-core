/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.store.entity.Goods;
import com.acooly.module.store.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品信息 管理控制器
 *
 * @author zhangpu
 * Date: 2017-08-21 01:56:35
 */
@Controller
@RequestMapping(value = "/manage/store/goods")
public class GoodsManagerController extends AbstractJQueryEntityController<Goods, GoodsService> {


    @SuppressWarnings("unused")
    @Autowired
    private GoodsService goodsService;

    {
        allowMapping = "*";
    }


}
