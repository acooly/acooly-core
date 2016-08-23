/**
 * create by zhangpu
 * date:2015年3月16日
 */
package com.acooly.core.common.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.acooly.core.common.dao.jdbc.PagedJdbcTemplate;

/**
 * @author zhangpu
 *
 */
public class BusinessServiceTest extends SpringTests {

	@Autowired
	private PagedJdbcTemplate pagedJdbcTemplate;

	@Test
	public void testOne() {
		System.out.println("Test One");
	}

}
