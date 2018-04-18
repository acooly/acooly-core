/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.point.dao.PointAccountDao;
import com.acooly.module.point.domain.PointAccount;
import com.acooly.module.point.domain.PointGrade;
import com.acooly.module.point.service.PointAccountService;
import com.acooly.module.point.service.PointGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分账户 Service实现
 *
 * <p>Date: 2017-02-03 22:45:13
 *
 * @author cuifuqiang
 */
@Service("pointAccountService")
public class PointAccountServiceImpl extends EntityServiceImpl<PointAccount, PointAccountDao>
        implements PointAccountService {

    @Autowired
    private PointGradeService pointGradeService;

    @Override
    public PointAccount findByUserName(String userName) {
        return getEntityDao().findByUserName(userName);
    }

    @Override
    @Transactional
    public PointAccount pointProduce(String userName, long point) {
        PointAccount pointAccount = findByUserNameForUpdate(userName);
        if (pointAccount == null) {
            pointAccount = new PointAccount();
            pointAccount.setUserName(userName);
            pointAccount.setBalance(pointAccount.getBalance() + point);
            pointAccount.setTotalProducePoint(pointAccount.getTotalProducePoint() + point);
            PointGrade pointGrade = pointGradeService.getSectionPoint(pointAccount);
            pointAccount.setGradeId(pointGrade.getId());
            getEntityDao().create(pointAccount);
        } else {
            pointAccount.setBalance(pointAccount.getBalance() + point);
            pointAccount.setTotalProducePoint(pointAccount.getTotalProducePoint() + point);
            PointGrade pointGrade = pointGradeService.getSectionPoint(pointAccount);
            pointAccount.setGradeId(pointGrade.getId());
            getEntityDao().update(pointAccount);
        }

        return pointAccount;
    }

    @Override
    @Transactional
    public PointAccount pointExpense(String userName, long point, boolean isFreeze) {
        PointAccount pointAccount = findByUserNameForUpdate(userName);
        if (pointAccount == null) {
            throw new BusinessException(userName + "积分账户不存在,无法完成积分消费");
        }
        if (isFreeze) {
            if (pointAccount.getFreeze() < point) {
                throw new BusinessException(userName + "冻结积分不足,无法完成积分消费");
            }
            pointAccount.setFreeze(pointAccount.getFreeze() - point);
        }
        pointAccount.setBalance(pointAccount.getBalance() - point);
        pointAccount.setTotalExpensePoint(pointAccount.getTotalExpensePoint() + point);
        if (pointAccount.getBalance() - pointAccount.getFreeze() < 0) {
            throw new BusinessException(userName + ":积分余额不足,无法完成积分消费");
        }
        PointGrade pointGrade = pointGradeService.getSectionPoint(pointAccount);
        pointAccount.setGradeId(pointGrade.getId());
        getEntityDao().update(pointAccount);
        return pointAccount;
    }

    @Override
    @Transactional
    public PointAccount pointFreeze(String userName, long point) {
        PointAccount pointAccount = findByUserNameForUpdate(userName);
        if (pointAccount == null) {
            throw new BusinessException(userName + "积分账户不存在,无法冻结");
        }
        pointAccount.setFreeze(pointAccount.getFreeze() + point);
        if (pointAccount.getBalance() - pointAccount.getFreeze() < 0) {
            throw new BusinessException(userName + "积分余额不足");
        }
        getEntityDao().update(pointAccount);
        return pointAccount;
    }

    @Override
    @Transactional
    public PointAccount pointUnfreeze(String userName, long point) {
        PointAccount pointAccount = findByUserNameForUpdate(userName);
        if (pointAccount == null) {
            throw new BusinessException(userName + "积分账户不存在,无法解冻");
        }
        if (pointAccount.getFreeze() < point) {
            throw new BusinessException(userName + "冻结积分不足,无法解冻");
        }
        pointAccount.setFreeze(pointAccount.getFreeze() - point);
        getEntityDao().update(pointAccount);
        return pointAccount;
    }

    @Override
    public int pointRank(String userName, Long gradeId) {
        int pointRank = 1;
        if (gradeId != null) {
            pointRank = getEntityDao().pointRankByUserNameAndGradeId(userName, gradeId);
        } else {
            pointRank = getEntityDao().pointRankByUserName(userName);
        }
        return pointRank;
    }

    @Override
    public PointAccount findByUserNameForUpdate(String userName) {
        return getEntityDao().findByUserNameForUpdate(userName);
    }
}
