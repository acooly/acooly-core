package com.acooly.module.security.dao.extend;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import com.acooly.core.common.dao.jdbc.PagedJdbcTemplate;
import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.security.domain.User;
import org.springframework.stereotype.Service;

/**
 * JPA方案中，对特殊需求的DAO的扩展实现DEMO
 * 
 * 要求： <br>
 * 1.另外定义接口UserDaoCustom <br>
 * 2.实现新定义的扩展接口，但名次必须命名为JAP接口名+Impl后缀
 * 
 * @author zhangpu
 * 
 */
public class UserDaoImpl implements UserDaoCustom {

	@Autowired
	PagedJdbcTemplate pagedJdbcTemplate;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	/**
	 * 使用JDBC操作数据库 Demo
	 */
	@Override
	public User findByJdbcTemplate(Long id) {

		String sql = "select * from SYS_USER where id = ?";
		User user = null;

		// 1.使用SqlRowSet
		// SqlRowSet rowSet = pagedJdbcTemplate.queryForRowSet(sql, new Object[]
		// { id });
		// if (rowSet.next()) {
		// user = new User();
		// user.setId(rowSet.getLong(1));
		// user.setLoginid(rowSet.getString(2));
		// user.setPasswd(rowSet.getString(3));
		// user.setName(rowSet.getString(4));
		// user.setEmail(rowSet.getString(5));
		// // ......
		// }

		// 使用RowMapper
		List<User> users = pagedJdbcTemplate.query(sql, new Object[] { id }, BeanPropertyRowMapper.newInstance(User.class));
		user = users.get(0);
		return user;
	}

	@Override
	public PageInfo<User> customeQueryWithPagedJdbcTemplate(PageInfo<User> pageInfo, Map<String, String> map) {
		String sql = "select * from SYS_USER where 1=1";
		String status = map.get("EQ_status");
		if (StringUtils.isNotBlank(status)) {
			sql += " and status = '" + status + "'";
		}
		String loginid = map.get("LIKE_loginid");
		if (StringUtils.isNotBlank(loginid)) {
			sql += " and loginid like '" + loginid + "%'";
		}
		pageInfo = pagedJdbcTemplate.queryOracle(pageInfo, sql, User.class);
		return pageInfo;
	}

	/**
	 * 使用JAP的EntityManager进行操作
	 */
	@Override
	public User findByCustomJap(Long id) {
		EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
		User user = em.find(User.class, id);
		EntityManagerFactoryUtils.closeEntityManager(em);
		return user;
	}

	@Override
	public User findByHibernate4(Long id) {
		throw new UnsupportedOperationException("Spring3-JAP整合方案不支持直接使用Hibernate");
	}

}
