/**
 * create by zhangpu
 * date:2015年11月30日
 */
package com.acooly.module.lottery.facade;

import com.acooly.module.lottery.dto.LotteryOrder;
import com.acooly.module.lottery.dto.LotteryResult;

/**
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

}
