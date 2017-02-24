package com.acooly.component.portlet.service.impl;

import com.acooly.component.portlet.dao.FeedbackDao;
import com.acooly.component.portlet.domain.Feedback;
import com.acooly.component.portlet.service.FeedbackService;
import com.acooly.core.common.service.EntityServiceImpl;
import org.springframework.stereotype.Service;


@Service("feedbackService")
public class FeedbackServiceImpl extends EntityServiceImpl<Feedback, FeedbackDao> implements FeedbackService {

}
