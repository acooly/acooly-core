/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 *
 */
package com.acooly.module.store.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.store.entity.Category;

import java.util.List;

/**
 * 品类信息 Service接口
 * <p>
 * Date: 2017-08-21 01:56:34
 *
 * @author zhangpu
 */
public interface CategoryService extends EntityService<Category> {


    List<Category> findByStoreCode(String storeCode);

}
