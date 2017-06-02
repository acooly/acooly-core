package com.acooly.module.lottery.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.module.lottery.domain.LotteryAward;

import java.util.List;

/**
 * lottery_award JPA Dao
 *
 * <p>Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 */
public interface LotteryAwardDao extends EntityJpaDao<LotteryAward, Long> {

  List<LotteryAward> findBylotteryId(Long lotteryId);

  LotteryAward findByCode(String code);
}
