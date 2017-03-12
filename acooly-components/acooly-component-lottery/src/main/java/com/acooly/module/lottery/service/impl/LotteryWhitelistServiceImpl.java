package com.acooly.module.lottery.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.module.lottery.dao.LotteryWhitelistDao;
import com.acooly.module.lottery.domain.LotteryWhitelist;
import com.acooly.module.lottery.enums.LotteryWhitelistStatus;
import com.acooly.module.lottery.service.LotteryWhitelistService;

@Service("lotteryWhitelistService")
public class LotteryWhitelistServiceImpl extends EntityServiceImpl<LotteryWhitelist, LotteryWhitelistDao> implements
		LotteryWhitelistService {

	@Override
	public LotteryWhitelist getValid(Long lotteryId, String user) {
		List<LotteryWhitelist> lists = getEntityDao().findByLotteryIdAndUserAndStatus(lotteryId, user,
				LotteryWhitelistStatus.enable);
		if (Collections3.isEmpty(lists)) {
			return null;
		}
		return Collections3.getFirst(lists);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveInNewTrans(LotteryWhitelist whitelist) {
		save(whitelist);
	}

}
