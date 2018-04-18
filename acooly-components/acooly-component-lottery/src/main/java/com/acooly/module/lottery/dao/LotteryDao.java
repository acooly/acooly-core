package com.acooly.module.lottery.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.module.lottery.domain.Lottery;

/**
 * lottery JPA Dao
 *
 * <p>Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 */
public interface LotteryDao extends EntityJpaDao<Lottery, Long> {

    Lottery findByCode(String code);
}
