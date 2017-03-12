package com.acooly.module.lottery.service;

import java.util.Map;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.lottery.domain.Lottery;
import com.acooly.module.lottery.dto.LotteryOrder;
import com.acooly.module.lottery.dto.LotteryResult;
import com.acooly.module.lottery.enums.LotteryStatus;

/**
 * lottery Service
 *
 * Date: 2014-12-12 04:40:07
 *
 * @author Acooly Code Generator
 *
 */
public interface LotteryService extends EntityService<Lottery> {

	Map<String, Object> lottery(String code, String user);

	Map<String, Object> lottery(String code, String user, String comments);

	/**
	 * 抽奖
	 * 
	 * @param order
	 * @return
	 */
	LotteryResult lottery(LotteryOrder order);

	Lottery findByCode(String code);

	Lottery updateStatus(Long id, LotteryStatus lotteryStatus);

}
