package com.acooly.module.lottery.portal;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.module.lottery.domain.Lottery;
import com.acooly.module.lottery.facade.LotteryFacade;
import com.acooly.module.lottery.facade.order.LotteryCountOrder;
import com.acooly.module.lottery.facade.order.LotteryOrder;
import com.acooly.module.lottery.facade.result.LotteryCountResult;
import com.acooly.module.lottery.facade.result.LotteryResult;
import com.acooly.module.lottery.service.LotteryService;
import com.acooly.module.lottery.service.LotteryWinnerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 抽奖活动 抽象实现
 *
 * @author zhangpu
 */
public abstract class AbstractLotteryPortalController
        extends AbstractStandardEntityController<Lottery, LotteryService> {

    @Resource
    protected LotteryFacade lotteryFacade;
    @Resource
    protected LotteryWinnerService lotteryWinnerService;

    /**
     * 抽奖
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("lottery")
    @ResponseBody
    public JsonEntityResult<LotteryResult> lottery(
            HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<LotteryResult> result = new JsonEntityResult<LotteryResult>();
        try {
            doLotteryCheck(request);
            LotteryOrder lotteryOrder =
                    new LotteryOrder(getLotteryCode(request), getLotteryUser(request), "测试");
            LotteryResult lotteryResult = lotteryFacade.lottery(lotteryOrder);
            result.setEntity(lotteryResult);
        } catch (Exception e) {
            handleException(result, "抽奖", e);
        }
        return result;
    }

    /**
     * 添加抽奖次数
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addCount")
    @ResponseBody
    public JsonResult addLotteryCount(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            String requCount = request.getParameter("count");
            if (Strings.isBlank(requCount) || !Strings.isNumeric(requCount)) {
                throw new RuntimeException("必须传入增加的次数参数：count");
            }
            LotteryCountOrder lotteryCountOrder =
                    new LotteryCountOrder(getLotteryCode(request), getLotteryUser(request));
            lotteryCountOrder.setCount(Integer.parseInt(requCount));
            lotteryCountOrder.setGid(Ids.gid());
            lotteryCountOrder.setPartnerId(Ids.getDid());
            ResultBase resultBase = lotteryFacade.addLotteryCount(lotteryCountOrder);
            result.setCode(resultBase.getCode());
            result.setMessage(resultBase.getDetail());
            result.setSuccess(resultBase.getStatus() == ResultStatus.success);
        } catch (Exception e) {
            handleException(result, "增加抽奖次数", e);
        }
        return result;
    }

    @RequestMapping("getCount")
    @ResponseBody
    public JsonResult getCount(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            LotteryCountOrder lotteryCountOrder =
                    new LotteryCountOrder(getLotteryCode(request), getLotteryUser(request));
            lotteryCountOrder.setGid(Ids.gid());
            lotteryCountOrder.setPartnerId(Ids.getDid());
            LotteryCountResult lotteryCountResult = lotteryFacade.getLotteryCount(lotteryCountOrder);
            result.setCode(lotteryCountResult.getCode());
            result.setMessage(lotteryCountResult.getDetail());
            result.setSuccess(lotteryCountResult.getStatus() == ResultStatus.success);
            result.appendData("totalTimes", lotteryCountResult.getTotalTimes());
            result.appendData("playTimes", lotteryCountResult.getPlayTimes());
        } catch (Exception e) {
            handleException(result, "查询抽奖次数", e);
        }
        return result;
    }

    protected abstract String getLotteryCode(HttpServletRequest request);

    protected abstract String getLotteryUser(HttpServletRequest request);

    /**
     * 根据具体抽奖逻辑check当前用户是否可以参与抽奖
     *
     * @param request
     */
    protected abstract void doLotteryCheck(HttpServletRequest request);
}
