/**
 * create by zhangpu
 * date:2015年11月30日
 */
package com.acooly.module.lottery.facade;

import com.acooly.module.lottery.facade.order.LotteryOrder;
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
     * 根据抽奖编码查询奖项
     *
     * @param lotteryCode
     * @return
     */
//    ListResult<LotteryAwardInfo> queryAward(SingleOrder<String> lotteryCode);


}
