package com.acooly.module.portlet.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.portlet.entity.Feedback;
import com.acooly.module.portlet.enums.FeedbackStatusEnum;

/**
 * 客户反馈 Service
 * <p>
 * Date: 2015-05-19 21:58:49
 *
 * @author Acooly Code Generator
 */
public interface FeedbackService extends EntityService<Feedback> {


    void handle(Long id, FeedbackStatusEnum status, String comments);


}
