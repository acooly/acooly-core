package com.acooly.module.lottery.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.lottery.dao.LotteryUserCountDao;
import com.acooly.module.lottery.domain.LotteryUserCount;
import com.acooly.module.lottery.service.LotteryUserCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("lotteryUserCountService")
public class LotteryUserCountServiceImpl
        extends EntityServiceImpl<LotteryUserCount, LotteryUserCountDao>
        implements LotteryUserCountService {

    @Transactional
    @Override
    public LotteryUserCount findAndLockUniqueUser(Long lotteryId, String user) {
        return getEntityDao().findByLotteryIdAndUser(lotteryId, user);
    }

    @Transactional
    @Override
    public void appendTimes(
            Long lotteryId, String lotteryCode, String lotteryTitle, String user, int times) {
        LotteryUserCount luc = findAndLockUniqueUser(lotteryId, user);
        if (luc == null) {
            luc = new LotteryUserCount();
            luc.setLotteryId(lotteryId);
            luc.setLotteryCode(lotteryCode);
            luc.setLotteryTitle(lotteryTitle);
            luc.setUser(user);
        }
        luc.setTotalTimes(luc.getTotalTimes() + times);
        save(luc);
    }

    @Override
    public LotteryUserCount findUniqueUser(String lotteryCode, String user) {
        return getEntityDao().findByLotteryCodeAndUser(lotteryCode, user);
    }
}
