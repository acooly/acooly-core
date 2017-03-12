package com.acooly.module.lottery.dao;

import java.math.BigInteger;

import javax.persistence.LockModeType;

import com.acooly.module.jpa.EntityJpaDao;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.acooly.module.lottery.domain.LotteryCount;

/**
 * lottery_count JPA Dao
 *
 * Date: 2015-11-28 17:08:35
 *
 * @author Acooly Code Generator
 *
 */
public interface LotteryCountDao extends EntityJpaDao<LotteryCount, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public LotteryCount findByUkey(String key);

	@Modifying
	@Query(value = "update lottery_count set count = count + 1 where ukey = ?1", nativeQuery = true)
	public void appendCount(String key);

	@Query(value = "select sum(count) from lottery_count where lottery_id = ?1", nativeQuery = true)
	public BigInteger getCount(Long lotteryId);

}
