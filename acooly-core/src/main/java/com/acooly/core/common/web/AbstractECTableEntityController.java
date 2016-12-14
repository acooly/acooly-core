package com.acooly.core.common.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.limit.Limit;
import org.extremecomponents.table.limit.LimitFactory;
import org.extremecomponents.table.limit.TableLimit;
import org.extremecomponents.table.limit.TableLimitFactory;
import org.springframework.ui.Model;
import org.springframework.web.util.WebUtils;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.service.EntityService;
import com.google.common.collect.Maps;

/**
 * 基于标准实体控制器实现对ECTABLE最为前端分页组件的基础控制器
 * 
 * <p>
 * 主要功能是实现对ECTABLE分页查询相关的参数进行封装
 * 
 * @author zhangpu
 * 
 * @param <T>
 * @param <M>
 */

public abstract class AbstractECTableEntityController<T extends AbstractEntity, M extends EntityService<T>>
		extends AbstractStandardEntityController<T, M> {

	@Override
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		try {
			model.addAllAttributes(referenceData(request));
			PageInfo<T> pageInfo = doList(request, response, model);
			model.addAttribute(getEntityListName(), pageInfo.getPageResults());
			request.setAttribute("totalRows",
					Integer.valueOf(String.valueOf(pageInfo.getTotalCount())));
		} catch (Exception e) {
			handleException("分页查询", e, request);
		}
		return getListView();
	}

	@Override
	protected PageInfo<T> getPageInfo(HttpServletRequest request) {
		Context context = new HttpServletRequestContext(request);
		LimitFactory limitFactory = new TableLimitFactory(context);
		Limit limit = new TableLimit(limitFactory);
		PageInfo<T> pageinfo = new PageInfo<T>();
		pageinfo.setCountOfCurrentPage(getDefaultPageSize());
		String ecp = request.getParameter("ec_p");
		String ecrd = request.getParameter("ec_rd");
		if (StringUtils.isNotBlank(ecp)) {
			pageinfo.setCurrentPage(Integer.parseInt(ecp));
		} else {
			pageinfo.setCurrentPage(limit.getPage());
		}
		if (StringUtils.isNotBlank(ecrd)) {
			pageinfo.setCountOfCurrentPage(Integer.parseInt(ecrd));
		}
		return pageinfo;
	}

	@Override
	protected Map<String, Boolean> getSortMap(HttpServletRequest request) {
		Map<String, Object> ectables = WebUtils.getParametersStartingWith(
				request, "ec");
		Map<String, Boolean> sortMap = Maps.newHashMap();
		for (Map.Entry<String, Object> entry : ectables.entrySet()) {
			if (entry.getKey().startsWith("_s_")
					&& StringUtils.isNotBlank((String) entry.getValue())) {
				sortMap.put(StringUtils.substringAfter(entry.getKey(), "_s_"),
						StringUtils.equalsIgnoreCase((String) entry.getValue(),
								"asc"));
			}
		}
		return sortMap;
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) {
		/** ec_table default setting start */
		String ecp = request.getParameter("ec_p");
		if (StringUtils.isNotBlank(ecp)) {
			request.setAttribute("ec_p", ecp);
		}
		request.setAttribute("totalRows", 0);
		/** ec_table default setting end */
		return super.referenceData(request);
	}

}
