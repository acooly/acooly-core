/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by cuifuqiang
 * date:2017-02-03
 */
package com.acooly.module.point.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Collections3;
import com.acooly.module.point.domain.PointAccount;
import com.acooly.module.point.domain.PointGrade;
import com.acooly.module.point.dto.PointTradeDto;
import com.acooly.module.point.enums.PointAccountStatus;
import com.acooly.module.point.service.PointAccountService;
import com.acooly.module.point.service.PointGradeService;
import com.acooly.module.point.service.PointTradeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 积分账户 管理控制器
 *
 * @author cuifuqiang Date: 2017-02-03 22:45:13
 */
@Controller
@RequestMapping(value = "/manage/point/pointAccount")
public class PointAccountManagerController
        extends AbstractJQueryEntityController<PointAccount, PointAccountService> {

    @SuppressWarnings("unused")
    @Autowired
    private PointAccountService pointAccountService;
    @Autowired
    private PointGradeService pointGradeService;
    @Autowired
    private PointTradeService pointTradeService;

    {
        allowMapping = "*";
    }

    @RequestMapping(value = {"importJson"})
    @ResponseBody
    public JsonResult importJson(
            HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonResult result = new JsonResult();
        allow(request, response, MappingMethod.imports);
        try {
            List<PointAccount> entities = doImport(request, response);
            result.setMessage("积分发放成功，成功发放用户数：" + entities.size() + ",关闭页面,刷新查看发放结果");
        } catch (Exception e) {
            handleException(result, "Excel导入", e);
        }
        return result;
    }

    protected List<PointAccount> doImport(
            HttpServletRequest request, HttpServletResponse response, FileType fileType)
            throws Exception {
        List<PointAccount> lists = Lists.newArrayList();
        Map<String, UploadResult> uploadResults = doUpload(request);
        List<List<String>> lineLists = loadImportFile(uploadResults, fileType, "UTF-8");
        System.out.println(lineLists);
        for (List<String> lines : lineLists) {
            String userName = lines.get(0);
            String point = lines.get(1);
            String memo = lines.get(2);
            PointTradeDto pointTradeDto = new PointTradeDto();
            pointTradeDto.setBusiData(memo);
            pointTradeDto.setMemo(memo);
            pointTradeService.pointProduce(userName, Long.parseLong(point), pointTradeDto);
            lists.add(pointAccountService.findByUserName(userName));
        }
        return lists;
    }

    @RequestMapping(value = "grantJson")
    @ResponseBody
    public JsonEntityResult<PointAccount> grantJson(
            HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<PointAccount> result = new JsonEntityResult<PointAccount>();
        allow(request, response, MappingMethod.create);
        try {
            String userNames = request.getParameter("userNames");
            String point = request.getParameter("point");
            String memo = request.getParameter("memo");
            List<String> userNameList = Lists.newArrayList(StringUtils.split(userNames, ","));
            if (Collections3.isEmpty(userNameList)) {
                throw new RuntimeException("用户名不能为空，多个用户名使用英文逗号分隔");
            }
            if (userNameList.size() > 50) {
                throw new RuntimeException("最多支持50个用户名");
            }
            for (String userName : userNameList) {
                PointTradeDto pointTradeDto = new PointTradeDto();
                pointTradeDto.setBusiData(memo);
                pointTradeDto.setMemo(memo);
                pointTradeService.pointProduce(userName, Long.parseLong(point), pointTradeDto);
            }
            result.setMessage("积分发放成功");
        } catch (Exception e) {
            handleException(result, "新增", e);
        }
        return result;
    }

    /**
     * 积分发放页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "grant")
    public String grant(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("积分发放", e, request);
        }
        return getListView() + "Grant";
    }

    @RequestMapping(value = "clearJson")
    @ResponseBody
    public JsonEntityResult<PointAccount> clearJson(
            HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<PointAccount> result = new JsonEntityResult<PointAccount>();
        allow(request, response, MappingMethod.create);
        try {
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String memo = request.getParameter("memo");

            PointTradeDto pointTradeDto = new PointTradeDto();
            pointTradeDto.setBusiId("0");
            pointTradeDto.setBusiType("pointClear");
            pointTradeDto.setBusiTypeText("清分清零");
            pointTradeDto.setBusiData(memo);
            pointTradeDto.setMemo(memo);
            pointTradeService.pointClearThread(startTime, endTime, pointTradeDto);
            result.setMessage("积分清零正在处理中,之后刷新查看结果");
        } catch (Exception e) {
            handleException(result, "新增", e);
        }
        return result;
    }

    /**
     * 积分清零
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "clear")
    public String clear(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("积分清零", e, request);
        }
        return getListView() + "Clear";
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allStatuss", PointAccountStatus.mapping());
        model.put("allPointGrades", getPointGradeMap());
    }

    public Map<Long, String> getPointGradeMap() {
        Map<Long, String> map = Maps.newLinkedHashMap();
        for (PointGrade pointGrade : pointGradeService.getAll()) {
            map.put(pointGrade.getId(), pointGrade.getTitle());
        }
        return map;
    }
}
