package com.acooly.module.lottery.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Money;
import com.acooly.module.lottery.domain.LotteryWinner;
import com.acooly.module.lottery.enums.LotteryAwardType;
import com.acooly.module.lottery.enums.WinnerStatus;
import com.acooly.module.lottery.service.LotteryAwardService;
import com.acooly.module.lottery.service.LotteryService;
import com.acooly.module.lottery.service.LotteryWinnerService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/module/lottery/lotteryWinner")
public class LotteryWinnerManagerController
        extends AbstractJQueryEntityController<LotteryWinner, LotteryWinnerService> {

    @Autowired
    private LotteryWinnerService lotteryWinnerService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private LotteryAwardService lotteryAwardService;

    @Override
    protected List<String> doExportEntity(LotteryWinner entity) {
        List<String> row = Lists.newArrayList();
        row.add(String.valueOf(entity.getId()));
        row.add(entity.getLotteryTitle());
        row.add(entity.getWinner());
        row.add(entity.getAwardType().getMessage());
        row.add(entity.getAward());
        row.add(Money.cent(entity.getAwardAmount()).toString());
        row.add(Dates.format(entity.getCreateTime()));
        row.add(entity.getStatus().getMessage());
        return row;
    }


    @Override
    protected List<String> getExportTitles() {
        return Lists.newArrayList("ID", "活动名称", "抽奖人", "类型", "奖项", "金额", "抽奖时间", "状态");
    }

    @Override
    protected String getExportFileName(HttpServletRequest request) {
        return super.getExportFileName(request);
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allStatuss", WinnerStatus.mapping());
        model.put("allLotterys", lotteryService.getAll());
        model.put("allAwardTypes", LotteryAwardType.mapping());
    }

}
