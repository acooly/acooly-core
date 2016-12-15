/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 01:22 创建
 */
package com.acooly.core.test.service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.test.dal.App;
import com.acooly.core.test.dal.AppRepository;
import org.springframework.stereotype.Service;

/**
 * @author qiubo@yiji.com
 */
@Service
public class AppEntityService extends EntityServiceImpl<App, AppRepository> {
}
