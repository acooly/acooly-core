package com.acooly.module.lottery.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.lottery.dao.LotteryCountDao;
import com.acooly.module.lottery.domain.LotteryCount;
import com.acooly.module.lottery.service.LotteryCountService;

@Service("lotteryCountService")
public class LotteryCountServiceImpl extends EntityServiceImpl<LotteryCount, LotteryCountDao> implements
		LotteryCountService {

	@Override
	@Transactional
	public LotteryCount loadAndLock(String ukey) {
		return getEntityDao().findByUkey(ukey);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveInNewTrans(LotteryCount lotteryCount) {
		getEntityDao().save(lotteryCount);
	}

	@Override
	public LotteryCount load(String ukey) {
		return getEntityDao().findUniqu("EQ_ukey", ukey);
	}

	@Override
	@Transactional
	public void appendCount(String ukey) {
		getEntityDao().appendCount(ukey);
	}

	@Override
	public int getCount(Long lotteryId) {
		return getEntityDao().getCount(lotteryId).intValue();
	}

}
