package com.acooly.module.lottery.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.lottery.dao.LotteryUserCountDao;
import com.acooly.module.lottery.domain.LotteryUserCount;
import com.acooly.module.lottery.service.LotteryUserCountService;

@Service("lotteryUserCountService")
public class LotteryUserCountServiceImpl extends EntityServiceImpl<LotteryUserCount, LotteryUserCountDao> implements LotteryUserCountService {

	@Transactional
	@Override
	public LotteryUserCount findAndLockUniqueUser(Long lotteryId,String user) {
		return getEntityDao().findByLotteryIdAndUser(lotteryId,user);
	}

	@Transactional
	@Override
	public void appendTimes(Long lotteryId,String lotteryTitle, String user, int times) {
		LotteryUserCount luc = findAndLockUniqueUser(lotteryId, user);
		if(luc == null){
			luc = new LotteryUserCount();
			luc.setLotteryId(lotteryId);
			luc.setLotteryTitle(lotteryTitle);
			luc.setUser(user);
		}
		luc.setTotalTimes(luc.getTotalTimes() + times);
		save(luc);
	}

	
	
}
