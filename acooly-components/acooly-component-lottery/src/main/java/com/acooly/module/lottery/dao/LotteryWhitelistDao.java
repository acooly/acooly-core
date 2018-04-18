package com.acooly.module.lottery.dao;

import com.acooly.module.jpa.EntityJpaDao;
import com.acooly.module.lottery.domain.LotteryWhitelist;
import com.acooly.module.lottery.enums.LotteryWhitelistStatus;

import java.util.List;

/**
 * lottery_whitelist JPA Dao
 *
 * <p>Date: 2016-01-02 23:20:21
 *
 * @author Acooly Code Generator
 */
public interface LotteryWhitelistDao extends EntityJpaDao<LotteryWhitelist, Long> {

    List<LotteryWhitelist> findByLotteryIdAndStatus(Long lotteryId, LotteryWhitelistStatus status);

    List<LotteryWhitelist> findByLotteryIdAndUserAndStatus(
            Long lotteryId, String user, LotteryWhitelistStatus status);
}
