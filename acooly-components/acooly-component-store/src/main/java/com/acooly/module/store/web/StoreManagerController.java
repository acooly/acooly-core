/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.store.entity.Store;
import com.acooly.module.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 仓库信息 管理控制器
 *
 * @author zhangpu
 * Date: 2017-08-21 01:56:34
 */
@Controller
@RequestMapping(value = "/manage/store/store")
public class StoreManagerController extends AbstractJQueryEntityController<Store, StoreService> {


    @SuppressWarnings("unused")
    @Autowired
    private StoreService storeService;

    {
        allowMapping = "*";
    }


}
