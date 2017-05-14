package com.acooly.module.portlet.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.portlet.dao.FeedbackDao;
import com.acooly.module.portlet.entity.Feedback;
import com.acooly.module.portlet.enums.FeedbackStatusEnum;
import com.acooly.module.portlet.integration.FeedbackHandler;
import com.acooly.module.portlet.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;


@Service("feedbackService")
public class FeedbackServiceImpl extends EntityServiceImpl<Feedback, FeedbackDao> implements FeedbackService {


    @Autowired
    private FeedbackHandler feedbackHandler;

    @Transactional
    @Override
    public void handle(Long id, FeedbackStatusEnum status, String comments) {
        Feedback feedback = get(id);
        if (feedback == null) {
            throw new BusinessException("数据不存在", "Not_Exsit_Data");
        }
        feedback.setStatus(status);
        feedback.setComments(comments);
        save(feedback);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                feedbackHandler.handle(feedback);
            }
        });

    }
}
