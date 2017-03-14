/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-03-15 01:03 创建
 */
package com.acooly.core.test.lottery;

import com.acooly.module.event.EventHandler;
import com.acooly.module.lottery.event.LotteryEvent;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author acooly
 */
@EventHandler
public class LotteryEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LotteryEventHandler.class);

    /**
     * 抽奖事件处理（异步）
     *
     * 1、如果中奖的是现金：调用对用的逻辑发放awardValue对应值的现金。
     * 2、如果是虚拟产品：如积分或卡券，根据配置的awardValue发放对应的积分和卡券
     * 3、如果是实物商品：根据配置的awardValue的商品编号进行处理，或通过通用的后台中奖列表进行处理。
     *
     * 如果在这里处理自动发奖，请注入：lotteryWinnerService服务，变更发放的记录状态。
     *
     * @param lotteryEvent
     */
    @Handler(delivery = Invoke.Asynchronously)
    public void handleEventAsync(LotteryEvent lotteryEvent) {
        logger.info("中奖事件处理：lotteryEvent：{}", lotteryEvent);
    }

}
