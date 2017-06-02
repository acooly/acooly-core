package com.acooly.module.lottery.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.lottery.domain.LotteryWhitelist;

/**
 * lottery_whitelist Service
 *
 * <p>Date: 2016-01-02 23:20:21
 *
 * @author Acooly Code Generator
 */
public interface LotteryWhitelistService extends EntityService<LotteryWhitelist> {

  LotteryWhitelist getValid(Long lotteryId, String user);

  void saveInNewTrans(LotteryWhitelist whitelist);
}
