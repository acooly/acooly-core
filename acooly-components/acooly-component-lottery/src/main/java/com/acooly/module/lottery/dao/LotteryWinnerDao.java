package com.acooly.module.lottery.dao;

import java.math.BigInteger;

import com.acooly.module.jpa.EntityJpaDao;
import org.springframework.data.jpa.repository.Query;

import com.acooly.module.lottery.domain.LotteryWinner;

/**
 * lottery_winner JPA Dao
 *
 * Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 *
 */
public interface LotteryWinnerDao extends EntityJpaDao<LotteryWinner, Long> {
	@Query(value = "select count(id) from lottery_winner where lottery_id = ?1 and award_id = ?2", nativeQuery = true)
	BigInteger getWinnerCount(Long lotteryId, Long awardId);

	@Query(value = "select count(id) from lottery_winner where lottery_id = ?1 and winner = ?2", nativeQuery = true)
	BigInteger getWinners(Long lotteryId, String winner);

	@Query(value = "select count(id) from lottery_winner where lottery_id = ?1", nativeQuery = true)
	BigInteger getCount(Long lotteryId);
}
