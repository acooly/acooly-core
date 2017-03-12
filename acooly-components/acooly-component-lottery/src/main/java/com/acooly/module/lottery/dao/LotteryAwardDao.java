package com.acooly.module.lottery.dao;

import java.util.List;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.module.lottery.domain.LotteryAward;

/**
 * lottery_award JPA Dao
 *
 * Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 *
 */
public interface LotteryAwardDao extends EntityJpaDao<LotteryAward, Long> {

	List<LotteryAward> findBylotteryId(Long lotteryId);

	LotteryAward findByCode(String code);

}
