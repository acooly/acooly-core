package com.acooly.module.security.dao;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;

import com.acooly.core.common.dao.jpa.EntityJpaDao;
import com.acooly.module.security.dao.extend.ResourceCustomDao;
import com.acooly.module.security.domain.Resource;

public interface ResourceDao extends EntityJpaDao<Resource, Long>,
		ResourceCustomDao {
	/**
	 * 根据URL查询Resource
	 * 
	 * @param url
	 * @return
	 */
	Resource findByValue(String url) throws DataAccessException;

	@Query("select r from Resource r where id <= ?1")
	List<Resource> findTop(Long count);

	@Query("select count(*) from Resource where orderTime > ?1")
	long getCountByGtOrderTime(Date date);

}
