/**
 * create by zhangpu date:2015年11月30日
 */
package com.acooly.module.lottery.exception;

/**
 * 没有参与抽奖的机会(用户参奖计数为0)
 *
 * @author zhangpu
 * @date 2016年03月15日
 */
public class NotOpportunityLotteryException extends LotteryException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -67753355988037449L;

    /**
     * @param lotteryErrorCode
     */
    public NotOpportunityLotteryException() {
        super(LotteryErrorCode.NotOpportunity);
    }
}
