/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-05 10:42 创建
 */
package com.acooly.module.mybatis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@Import({ MapperScannerRegistrar.class })
public class MybatisAutoConfig {
}
