/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-01-16 10:56
 */
package com.acooly.core.test.mybatis;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.test.core.dao.AppDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author zhangpu
 * @date 2020-01-16 10:56
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = com.acooly.core.test.Main.class)
public class DeleteAppDaoTest {

    protected static final String PROFILE = "sdev";

    static {
        Apps.setProfileIfNotExists(PROFILE);
    }

    @Autowired
    private AppDao appDao;

    @Test
    public void testDelete() {
        appDao.remove(null);
    }

}
