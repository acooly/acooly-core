package com.acooly.module.lottery.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.lottery.domain.LotteryCount;

/**
 * lottery_count Service
 *
 * <p>Date: 2015-11-28 17:08:35
 *
 * @author Acooly Code Generator
 */
public interface LotteryCountService extends EntityService<LotteryCount> {

    LotteryCount loadAndLock(String ukey);

    void saveInNewTrans(LotteryCount lotteryCount);

    LotteryCount load(String ukey);

    void appendCount(String ukey);

    int getCount(Long lotteryId);
}
