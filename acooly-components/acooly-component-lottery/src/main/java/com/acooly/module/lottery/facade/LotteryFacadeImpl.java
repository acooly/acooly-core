/**
 * create by zhangpu date:2015年11月30日
 */
package com.acooly.module.lottery.facade;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.module.lottery.domain.Lottery;
import com.acooly.module.lottery.domain.LotteryUserCount;
import com.acooly.module.lottery.exception.LotteryException;
import com.acooly.module.lottery.exception.VoteLotteryException;
import com.acooly.module.lottery.facade.order.LotteryCountOrder;
import com.acooly.module.lottery.facade.order.LotteryOrder;
import com.acooly.module.lottery.facade.result.LotteryCountResult;
import com.acooly.module.lottery.facade.result.LotteryResult;
import com.acooly.module.lottery.service.LotteryService;
import com.acooly.module.lottery.service.LotteryUserCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangpu
 * @date 2015年11月30日
 */
@Service
public class LotteryFacadeImpl implements LotteryFacade {

    private static final Logger logger = LoggerFactory.getLogger(LotteryFacadeImpl.class);

    @Resource
    private LotteryService lotteryService;

    @Resource
    private LotteryUserCountService lotteryUserCountService;

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

    @Override
    public ResultBase addLotteryCount(LotteryCountOrder order) {
        ResultBase result = new ResultBase();
        try {
            order.check();
            Lottery lottery = lotteryService.findByCode(order.getLotteryCode());
            if (lottery == null) {
                logger.warn("没有编码对应的抽奖活动存在。 lotteryCode:{}", order.getLotteryCode());
                throw new BusinessException("没有编码对应的抽奖活动存在", "Lottery_Not_Exsit");
            }
            lotteryUserCountService.appendTimes(
                    lottery.getId(),
                    lottery.getCode(),
                    lottery.getTitle(),
                    order.getUsername(),
                    order.getCount());
        } catch (BusinessException be) {
            result.setStatus(ResultStatus.failure);
            result.setCode(be.getCode());
            result.setDetail(be.getMessage());
        } catch (Exception e) {
            logger.warn("添加抽奖次数失败。 lotteryCode:{}", order.getLotteryCode(), e);
            result.setStatus(ResultStatus.failure);
            result.setCode("System");
            result.setDetail("添加抽奖次数失败");
        }
        return result;
    }

    @Override
    public LotteryCountResult getLotteryCount(LotteryCountOrder order) {
        LotteryCountResult result = new LotteryCountResult();
        try {
            order.check();
            LotteryUserCount lotteryUserCount =
                    lotteryUserCountService.findUniqueUser(order.getLotteryCode(), order.getUsername());
            BeanCopier.copy(
                    lotteryUserCount,
                    result,
                    BeanCopier.CopyStrategy.IGNORE_NULL,
                    BeanCopier.NoMatchingRule.IGNORE);
            result.setUsername(lotteryUserCount.getUser());
        } catch (BusinessException be) {
            result.setStatus(ResultStatus.failure);
            result.setCode(be.getCode());
            result.setDetail(be.getMessage());
        } catch (Exception e) {
            logger.warn("添加抽奖次数失败。 lotteryCode:{}", order.getLotteryCode(), e);
            result.setStatus(ResultStatus.failure);
            result.setCode("System");
            result.setDetail("添加抽奖次数失败");
        }
        return result;
    }
}
