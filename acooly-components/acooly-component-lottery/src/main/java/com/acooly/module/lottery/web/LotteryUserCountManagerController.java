package com.acooly.module.lottery.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.utils.Dates;
import com.acooly.module.lottery.domain.LotteryUserCount;
import com.acooly.module.lottery.service.LotteryUserCountService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/manage/module/lottery/lotteryUserCount")
public class LotteryUserCountManagerController
        extends AbstractJQueryEntityController<LotteryUserCount, LotteryUserCountService> {

    @SuppressWarnings("unused")
    @Autowired
    private LotteryUserCountService lotteryUserCountService;

    @Override
    protected List<String> getExportTitles() {
        return Lists.newArrayList("ID", "活动ID", "活动标题", "参与人", "获参次数", "已参与次数", "剩余次数", "创建时间", "最后修改时间");
    }


    @Override
    protected List<String> doExportEntity(LotteryUserCount entity) {
        List<String> line = Lists.newArrayList();
        line.add(String.valueOf(entity.getId()));
        line.add(String.valueOf(entity.getLotteryId()));
        line.add(entity.getLotteryTitle());
        line.add(entity.getUser());
        line.add(String.valueOf(entity.getTotalTimes()));
        line.add(String.valueOf(entity.getPlayTimes()));
        line.add(String.valueOf(entity.getTotalTimes() - entity.getPlayTimes()));
        line.add(Dates.format(entity.getCreateTime()));
        line.add(Dates.format(entity.getUpdateTime()));
        return line;
    }

}
