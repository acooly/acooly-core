package com.acooly.module.lottery.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.enums.EntityStatus;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.utils.Money;
import com.acooly.module.lottery.domain.LotteryAward;
import com.acooly.module.lottery.enums.LotteryAwardType;
import com.acooly.module.lottery.enums.MaxPeriod;
import com.acooly.module.lottery.service.LotteryAwardService;

@Controller
@RequestMapping(value = "/manage/module/lottery/lotteryAward")
public class LotteryAwardManagerController extends AbstractJQueryEntityController<LotteryAward, LotteryAwardService> {

	@Autowired
	private LotteryAwardService lotteryAwardService;

	@Override
	protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("lotteryId", request.getParameter("lotteryId"));
	}

	@Override
	protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, LotteryAward entity) {
		model.addAttribute("lotteryId", entity.getLotteryId());
	}
	
	@Override
	protected LotteryAward onSave(HttpServletRequest request, HttpServletResponse response, Model model,
									LotteryAward entity, boolean isCreate) throws Exception {
		if(LotteryAwardType.money.equals(entity.getAwardType())){			
			entity.setAwardAmount(Money.amout(request.getParameter("awardAmount")).getCent());
		}else{
			entity.setAwardAmount(0l);
		}
		return super.onSave(request, response, model, entity, isCreate);   
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allMaxPeriods", allMaxPeriods);
		model.put("allRecordWinners", allRecordWinners);
		model.put("allAwardTypes", allAwardTypes);
	}

	private static Map<String, String> allMaxPeriods = MaxPeriod.mapping();
	private static Map<String, String> allRecordWinners = EntityStatus.mapping();
	private static Map<String, String> allAwardTypes = LotteryAwardType.mapping();

}
