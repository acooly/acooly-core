package com.acooly.module.lottery.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.lottery.service.LotteryAwardService;
import com.acooly.module.lottery.dao.LotteryAwardDao;
import com.acooly.module.lottery.domain.LotteryAward;

@Service("lotteryAwardService")
public class LotteryAwardServiceImpl extends EntityServiceImpl<LotteryAward, LotteryAwardDao> implements
		LotteryAwardService {

	@Override
	public List<LotteryAward> findBylotteryId(Long lotteryId) {
		return getEntityDao().findBylotteryId(lotteryId);
	}

	@Override
	public LotteryAward findByCode(String code) {
		return getEntityDao().findByCode(code);
	}

}
