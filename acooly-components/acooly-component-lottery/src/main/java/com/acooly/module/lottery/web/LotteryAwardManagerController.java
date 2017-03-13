package com.acooly.module.lottery.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.module.lottery.domain.LotteryAward;
import com.acooly.module.lottery.enums.LotteryAwardType;
import com.acooly.module.lottery.enums.MaxPeriod;
import com.acooly.module.lottery.service.LotteryAwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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

        if (Strings.isBlank(entity.getCode())) {
            entity.setCode(Ids.getDid());
        }
        if (LotteryAwardType.money.equals(entity.getAwardType())) {
            entity.setAwardAmount(Money.amout(request.getParameter("awardAmount")).getCent());
        } else {
            entity.setAwardAmount(0l);
        }
        if (entity.getMaxPeriod() == MaxPeriod.ulimit) {
            entity.setMaxWiner(0);
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
    private static Map<String, String> allRecordWinners = SimpleStatus.mapping();
    private static Map<String, String> allAwardTypes = LotteryAwardType.mapping();

}
