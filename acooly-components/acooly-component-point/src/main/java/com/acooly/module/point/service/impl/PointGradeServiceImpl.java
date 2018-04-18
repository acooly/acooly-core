/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.event.EventBus;
import com.acooly.module.point.dao.PointGradeDao;
import com.acooly.module.point.domain.PointAccount;
import com.acooly.module.point.domain.PointGrade;
import com.acooly.module.point.event.PointEvent;
import com.acooly.module.point.service.PointGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 积分等级 Service实现
 *
 * <p>Date: 2017-02-03 22:47:28
 *
 * @author cuifuqiang
 */
@Service("pointGradeService")
public class PointGradeServiceImpl extends EntityServiceImpl<PointGrade, PointGradeDao>
        implements PointGradeService {

    @Autowired
    private EventBus eventBus;

    @Override
    public PointGrade getSectionPoint(PointAccount pointAccount) {
        Long point = pointAccount.getTotalProducePoint();
        PointGrade pointGrade = getEntityDao().getSectionPoint(point);
        if (pointGrade == null) {
            throw new BusinessException("未找到匹配的积分用户等级");
        }

        PointEvent pointEvent = new PointEvent();
        pointEvent.setUserName(pointAccount.getUserName());
        pointEvent.setAvailable(pointAccount.getAvailable());
        pointEvent.setBalance(pointAccount.getBalance());
        pointEvent.setFreeze(pointAccount.getFreeze());
        pointEvent.setTotalExpensePoint(pointAccount.getTotalExpensePoint());
        pointEvent.setTotalProducePoint(pointAccount.getTotalProducePoint());
        pointEvent.setGradeId(pointGrade.getId());
        pointEvent.setGradeTitle(pointGrade.getTitle());
        eventBus.publishAfterTransactionCommitted(pointEvent);
        return pointGrade;
    }
}
