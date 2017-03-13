/**
 * create by zhangpu
 * date:2015年11月30日
 */
package com.acooly.module.lottery.facade;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.module.lottery.facade.order.LotteryCountOrder;
import com.acooly.module.lottery.facade.order.LotteryOrder;
import com.acooly.module.lottery.facade.result.LotteryCountResult;
import com.acooly.module.lottery.facade.result.LotteryResult;

/**
 * 抽奖活动facade
 *
 * @author zhangpu
 * @date 2015年11月30日
 */
public interface LotteryFacade {

    /**
     * 抽奖
     *
     * @param order
     * @return
     */
    LotteryResult lottery(LotteryOrder order);

    /**
     * 增加用户抽奖次数
     *
     * @param order
     * @return
     */
    ResultBase addLotteryCount(LotteryCountOrder order);

    /**
     * 查询用户的可抽奖次数
     *
     * @param order
     * @return
     */
    LotteryCountResult getLotteryCount(LotteryCountOrder order);

}
