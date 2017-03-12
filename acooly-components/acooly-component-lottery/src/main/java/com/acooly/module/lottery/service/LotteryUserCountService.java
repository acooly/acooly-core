package com.acooly.module.lottery.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.lottery.domain.LotteryUserCount;

/**
 * lottery_user_count Service
 *
 * Date: 2016-03-11 02:38:56
 *
 * @author Acooly Code Generator
 *
 */
public interface LotteryUserCountService extends EntityService<LotteryUserCount> {

	LotteryUserCount findAndLockUniqueUser(Long lotteryId,String user);
	
	void appendTimes(Long lotteryId, String lotteryTitle,String user, int times);
	
}
