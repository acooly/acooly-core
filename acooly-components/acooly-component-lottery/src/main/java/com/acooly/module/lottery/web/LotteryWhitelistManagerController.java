package com.acooly.module.lottery.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.lottery.domain.LotteryWhitelist;
import com.acooly.module.lottery.enums.LotteryWhitelistStatus;
import com.acooly.module.lottery.service.LotteryWhitelistService;

@Controller
@RequestMapping(value = "/manage/module/lottery/lotteryWhitelist")
public class LotteryWhitelistManagerController extends
		AbstractJQueryEntityController<LotteryWhitelist, LotteryWhitelistService> {

	private static Map<String, String> allStatuss = LotteryWhitelistStatus.mapping();

	@Autowired
	private LotteryWhitelistService lotteryWhitelistService;

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allStatuss", allStatuss);
	}

}
