package com.acooly.module.security.dao.extend;

import java.util.Map;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.security.domain.User;

public interface UserDaoCustom {

	User findByJdbcTemplate(Long id);

	User findByCustomJap(Long id);

	User findByHibernate4(Long id);

	PageInfo<User> customeQueryWithPagedJdbcTemplate(PageInfo<User> pageInfo, Map<String, String> map);

}
