package com.acooly.module.app.dao;

import java.util.List;

import com.acooly.module.app.domain.AppCustomer;
import com.acooly.module.app.enums.EntityStatus;
import com.acooly.module.jpa.EntityJpaDao;

/**
 * app_customer JPA Dao
 *
 * Date: 2015-05-12 13:39:30
 *
 * @author Acooly Code Generator
 *
 */
public interface AppCustomerDao extends EntityJpaDao<AppCustomer, Long> {

	List<AppCustomer> findByUserNameAndStatus(String userName, EntityStatus status);

}
