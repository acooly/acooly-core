package com.acooly.module.lottery.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.lottery.domain.LotteryAward;

import java.util.List;

/**
 * lottery_award Service
 *
 * <p>Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 */
public interface LotteryAwardService extends EntityService<LotteryAward> {
  List<LotteryAward> findBylotteryId(Long lotteryId);

  LotteryAward findByCode(String code);
}
