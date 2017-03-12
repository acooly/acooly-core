package com.acooly.module.lottery.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.utils.enums.SimpleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.core.common.enums.EntityStatus;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Ids;
import com.acooly.module.lottery.domain.Lottery;
import com.acooly.module.lottery.enums.LotteryStatus;
import com.acooly.module.lottery.enums.LotteryType;
import com.acooly.module.lottery.service.LotteryService;

@Controller
@RequestMapping(value = "/manage/module/lottery/lottery")
public class LotteryManagerController extends AbstractJQueryEntityController<Lottery, LotteryService> {

	@Autowired
	private LotteryService lotteryService;
	
	@Override
	protected Lottery onSave(HttpServletRequest request, HttpServletResponse response, Model model, Lottery entity,
			boolean isCreate) throws Exception {
		if (isCreate) {
			entity.setCode(Ids.getDid());
		}
		return entity;
	}

	@RequestMapping(value = "status")
	@ResponseBody
	public JsonEntityResult<Lottery> status(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<Lottery> result = new JsonEntityResult<Lottery>();
		try {
			Lottery lottery = loadEntity(request);
			LotteryStatus status = null;
			if (lottery.getStatus() == LotteryStatus.enable) {
				status = LotteryStatus.pause;
			} else if (lottery.getStatus() == LotteryStatus.pause) {
				status = LotteryStatus.enable;
			} else {
				throw new RuntimeException("活动已" + lottery.getStatus());
			}
			lottery.setStatus(status);
			getEntityService().save(lottery);
			result.setEntity(lottery);
			result.setMessage("状态成功修改为:" + status.getMessage());
		} catch (Exception e) {
			handleException(result, "状态修改", e);
		}
		return result;
	}






	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allTypes", LotteryType.mapping());
		model.put("allStatuss", LotteryStatus.mapping());
		model.put("allUserCounters", SimpleStatus.mapping());
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
		SimpleDateFormat dateFormat = new SimpleDateFormat(Dates.CHINESE_DATE_FORMAT_LINE);
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
