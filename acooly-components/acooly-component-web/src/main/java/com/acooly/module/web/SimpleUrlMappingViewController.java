/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-10-19 17:33 创建
 *
 */
package com.acooly.module.web;

import com.google.common.collect.Maps;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.AbstractUrlViewController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @author yanglie@yiji.com */
public class SimpleUrlMappingViewController extends AbstractUrlViewController {

  private Map<String, String> viewNameMap = Maps.newHashMap();

  @Override
  protected String getViewNameForRequest(HttpServletRequest request) {
    String url = extractOperableUrl(request);
    String viewName = viewNameMap.get(url);
    return viewName;
  }

  protected String extractOperableUrl(HttpServletRequest request) {
    String urlPath =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    if (!StringUtils.hasText(urlPath)) {
      urlPath = getUrlPathHelper().getLookupPathForRequest(request);
    }
    return urlPath;
  }

  public void setViewNameMap(Map<String, String> viewNameMap) {
    this.viewNameMap = viewNameMap;
  }
}
