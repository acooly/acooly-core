package com.acooly.module.lottery.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.module.lottery.domain.LotteryUserCount;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

/**
 * lottery_user_count JPA Dao
 *
 * <p>Date: 2016-03-11 02:38:56
 *
 * @author Acooly Code Generator
 */
public interface LotteryUserCountDao extends EntityJpaDao<LotteryUserCount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    LotteryUserCount findByLotteryIdAndUser(Long lotteryId, String user);

    LotteryUserCount findByLotteryCodeAndUser(String lotteryCode, String user);

    //	@Transactional
    //	@Modifying
    //	@Query("update LotteryUserCount l set l.totalTimes = l.totalTimes + ?3 where lotteryId = ?1 and user = ?2")
    //	void appendTimes(Long lotteryId, String user, int times);

}
