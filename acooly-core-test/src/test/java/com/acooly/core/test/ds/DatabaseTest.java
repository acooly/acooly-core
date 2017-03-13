/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing@yiji.com 2017-03-12 16:10 创建
 */
package com.acooly.core.test.ds;

import com.acooly.core.test.TestBase;
import org.apache.shiro.util.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

/**
 * @author shuijing
 */
public class DatabaseTest extends TestBase {

    @Autowired
    protected DataSource dataSource;
	@Test
	public void testDataSource() throws Exception {
        String url = dataSource.getConnection().getMetaData().getURL();
        Assert.notNull(url);
    }

	@AfterClass
	public static void testW() throws Exception {
		System.out.println("a");
	}
}
