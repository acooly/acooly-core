package com.acooly.module.portlet.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.portlet.domain.Feedback;
import com.acooly.module.portlet.enums.FeedbackType;
import com.acooly.module.portlet.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
@RequestMapping(value = "/manage/module/portlet/feedback")
public class FeedbackManagerController extends AbstractJQueryEntityController<Feedback, FeedbackService> {

	private static Map<String, String> allTypes = FeedbackType.mapping();

	@Autowired
	private FeedbackService feedbackService;

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allTypes", allTypes);
	}

}
