package com.acooly.module.portlet.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.portlet.dao.FeedbackDao;
import com.acooly.module.portlet.domain.Feedback;
import com.acooly.module.portlet.service.FeedbackService;
import org.springframework.stereotype.Service;


@Service("feedbackService")
public class FeedbackServiceImpl extends EntityServiceImpl<Feedback, FeedbackDao> implements FeedbackService {

}
