/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.store.service.StoreService;
import com.acooly.module.store.dao.StoreDao;
import com.acooly.module.store.entity.Store;

/**
 * 仓库信息 Service实现
 *
 * Date: 2017-08-21 01:56:34
 *
 * @author zhangpu
 *
 */
@Service("storeService")
public class StoreServiceImpl extends EntityServiceImpl<Store, StoreDao> implements StoreService {

}
