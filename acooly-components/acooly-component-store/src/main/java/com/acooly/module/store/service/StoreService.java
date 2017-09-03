/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 *
 */
package com.acooly.module.store.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.store.dto.StoreNode;
import com.acooly.module.store.entity.Store;

import java.util.List;
import java.util.Map;

/**
 * 仓库信息 Service接口
 * <p>
 * Date: 2017-08-21 01:56:34
 *
 * @author zhangpu
 */
public interface StoreService extends EntityService<Store> {

    /**
     * 根据仓库编码加载仓库库存结构
     *
     * @param storeCode
     * @return
     */
    List<StoreNode> loadStore(String storeCode);

    /**
     * 仓库结构（Map）
     *
     * @param storeCode
     * @return
     */
    List<Map<String, Object>> loadStoreMap(String storeCode);
}
