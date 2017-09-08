/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.store.dao.GoodsDao;
import com.acooly.module.store.entity.Goods;
import com.acooly.module.store.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品信息 Service实现
 * <p>
 * Date: 2017-08-21 01:56:35
 *
 * @author zhangpu
 */
@Service("goodsService")
public class GoodsServiceImpl extends EntityServiceImpl<Goods, GoodsDao> implements GoodsService {


    @Override
    public List<Goods> findByStoreCode(String storeCode) {
        return getEntityDao().findByStoreCode(storeCode);
    }
}
