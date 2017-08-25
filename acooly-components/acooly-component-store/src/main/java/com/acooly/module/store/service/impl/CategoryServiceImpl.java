/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.store.dao.CategoryDao;
import com.acooly.module.store.entity.Category;
import com.acooly.module.store.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品类信息 Service实现
 * <p>
 * Date: 2017-08-21 01:56:35
 *
 * @author zhangpu
 */
@Service("categoryService")
public class CategoryServiceImpl extends EntityServiceImpl<Category, CategoryDao> implements CategoryService {

    @Override
    public List<Category> findByStoreCode(String storeCode) {
        return getEntityDao().findByStoreCode(storeCode);
    }
}
