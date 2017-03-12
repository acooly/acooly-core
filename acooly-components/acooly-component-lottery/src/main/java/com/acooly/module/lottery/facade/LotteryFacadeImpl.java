/**
 * create by zhangpu
 * date:2015年11月30日
 */
package com.acooly.module.lottery.facade;

import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.module.lottery.exception.LotteryException;
import com.acooly.module.lottery.exception.VoteLotteryException;
import com.acooly.module.lottery.facade.order.LotteryOrder;
import com.acooly.module.lottery.facade.result.LotteryResult;
import com.acooly.module.lottery.service.LotteryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
                break;
            } catch (VoteLotteryException lve) {
                continue;
            } catch (LotteryException le) {
                result.setStatus(ResultStatus.failure);
                result.setCode(le.getCode());
                result.setDetail(le.getMessage());
                break;
            } catch (Exception e) {
                result.setStatus(ResultStatus.failure);
                result.setDetail(e.getMessage());
                break;
            }
        }
        return result;
    }

}
