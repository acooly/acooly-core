/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-18 15:18
 */
package com.acooly.core.common.dao.dialect.impl;

import com.acooly.core.common.dao.dialect.DatabaseType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpu
 * @date 2022-09-18 15:18
 */
@Slf4j
public class H2DatabaseDialect extends MySQLDatabaseDialect {

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.h2;
    }
}
