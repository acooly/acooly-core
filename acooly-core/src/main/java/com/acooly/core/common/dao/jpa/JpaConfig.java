/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-15 01:46 创建
 */
package com.acooly.core.common.dao.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableJpaRepositories(repositoryBaseClass = AbstractEntityJpaDao.class)
public class JpaConfig {
}
