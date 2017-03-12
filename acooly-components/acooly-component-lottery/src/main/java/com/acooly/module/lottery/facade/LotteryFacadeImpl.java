/**
 * create by zhangpu
 * date:2015年11月30日
 */
package com.acooly.module.lottery.facade;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.acooly.module.lottery.dto.LotteryOrder;
import com.acooly.module.lottery.dto.LotteryResult;
import com.acooly.module.lottery.exception.LotteryException;
import com.acooly.module.lottery.exception.VoteLotteryException;
import com.acooly.module.lottery.service.LotteryService;

/**
 * @author zhangpu
 * @date 2015年11月30日
 */
@Service
public class LotteryFacadeImpl implements LotteryFacade {

	@Resource
	private LotteryService lotteryService;

	@Override
	public LotteryResult lottery(LotteryOrder order) {
		LotteryResult result = new LotteryResult();
		while (true) {
			try {
				result = lotteryService.lottery(order);
				result.setSuccess(true);
				break;
			} catch (VoteLotteryException lve) {
				continue;
			} catch (LotteryException le) {
				result.setSuccess(false);
				result.setCode(le.getCode());
				result.setMessage(le.getMessage());
				break;
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMessage(e.getMessage());
				break;
			}
		}
		return result;
	}
}
