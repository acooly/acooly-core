package com.acooly.module.lottery.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.lottery.dao.LotteryWinnerDao;
import com.acooly.module.lottery.domain.LotteryWinner;
import com.acooly.module.lottery.service.LotteryWinnerService;
import org.springframework.stereotype.Service;

@Service("lotteryWinnerService")
public class LotteryWinnerServiceImpl extends EntityServiceImpl<LotteryWinner, LotteryWinnerDao>
    implements LotteryWinnerService {

  @Override
  public int count(Long lotteryId, Long awardId) {
    return getEntityDao().getWinnerCount(lotteryId, awardId).intValue();
  }

  @Override
  public int getWinnerCount(Long lotteryId, String winner) {
    return getEntityDao().getWinners(lotteryId, winner).intValue();
  }

  @Override
  public int getCount(Long lotteryId) {
    return getEntityDao().getCount(lotteryId).intValue();
  }
}
