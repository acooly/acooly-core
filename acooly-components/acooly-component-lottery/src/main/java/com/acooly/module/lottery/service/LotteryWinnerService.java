package com.acooly.module.lottery.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.lottery.domain.LotteryWinner;

/**
 * lottery_winner Service
 *
 * Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 *
 */
public interface LotteryWinnerService extends EntityService<LotteryWinner> {
	int count(Long lotteryId, Long awardId);

	int getWinnerCount(Long lotteryId, String winner);

	int getCount(Long lotteryId);
}
