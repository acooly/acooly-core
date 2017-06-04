/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by shuijing
 * date:2017-05-26
 */
package com.acooly.module.security.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.module.security.domain.Org;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.enums.OrgStatus;
import com.acooly.module.security.service.OrgService;
import com.acooly.module.security.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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
 * 组织机构 管理控制器
 *
 * @author shuijing Date: 2017-05-26 16:48:57
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/module/security/org")
public class OrgManagerController extends AbstractJQueryEntityController<Org, OrgService> {

  @Autowired private OrgService orgService;

  {
    allowMapping = "*";
  }

  protected User getSessionUser() {
    return (User) SecurityUtils.getSubject().getPrincipal();
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allStatuss", OrgStatus.mapping());
  }

  @RequestMapping(value = "listTree")
  @ResponseBody
  public JsonListResult<Org> getTopLevel(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    JsonListResult<Org> result = new JsonListResult<>();
    try {
      result.appendData(referenceData(request));
      Long orgId = Long.valueOf(0);
      if (getSessionUser().getUserType() != 1) {
        orgId = ShiroUtils.getCurrentUser().getOrgId();
      }
      log.info("用户组织机构id为{}", orgId);
      List<Org> organizes = orgService.getTreeList(orgId);
      result.setTotal(Long.valueOf(organizes.size()));
      result.setRows(organizes);
    } catch (Exception e) {
      handleException(new JsonResult(), "机构管理分类树查询", e);
    }
    return result;
  }

  @RequestMapping(value = "listOrganize")
  @ResponseBody
  public String getListOrganize(HttpServletRequest request, HttpServletResponse response) {
    String organizesJson = "";
    try {
      Long orgId = Long.valueOf(0);
      if (getSessionUser().getUserType() != 1) {
        orgId = ShiroUtils.getCurrentUser().getOrgId();
      }
      log.info("用户组织机构id为{}", orgId);
      List<Org> organizes = orgService.getTreeList(orgId);
      organizesJson = JsonMapper.nonEmptyMapper().toJson(organizes);

    } catch (Exception e) {
      handleException(new JsonResult(), "机构管理提供机构树查列表", e);
    }
    return organizesJson;
  }

  @Override
  protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("parentId", request.getParameter("parentId"));
  }

  @Override
  protected Org onSave(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      Org entity,
      boolean isCreate)
      throws Exception {

    String parentId = request.getParameter("parentId");
    if (StringUtils.isNotBlank(parentId)) {
      entity.setParentId(Long.parseLong(parentId));
    }
    return entity;
  }

  @Override
  public JsonEntityResult<Org> updateJson(
      HttpServletRequest request, HttpServletResponse response) {
    return super.updateJson(request, response);
  }
}