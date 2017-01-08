/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-09-10 11:23 创建
 *
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo@yiji.com
 */
public class MybatisComponentInitializer implements ComponentInitializer {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		setPropertyIfMissing("mapper.mappers", "com.acooly.module.mybatis.EntityMybatisDao");
	}
}
