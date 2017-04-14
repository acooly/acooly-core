package com.acooly.module.portlet.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.portlet.dao.FeedbackDao;
import com.acooly.module.portlet.entity.Feedback;
import com.acooly.module.portlet.enums.FeedbackStatusEnum;
import com.acooly.module.portlet.integration.FeedbackHandler;
import com.acooly.module.portlet.service.FeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("feedbackService")
public class FeedbackServiceImpl extends EntityServiceImpl<Feedback, FeedbackDao> implements FeedbackService {


    @Override
    public void handle(Long id, FeedbackStatusEnum status, String comments) {

        Feedback feedback = get(id);
        if (feedback == null) {
            throw new BusinessException("数据不存在", "Not_Exsit_Data");
        }

        feedback.setStatus(status);
        feedback.setComments(comments);

        save(feedback);
    }
}
