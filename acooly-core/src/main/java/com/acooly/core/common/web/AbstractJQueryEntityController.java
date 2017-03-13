package com.acooly.core.common.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.domain.Entityable;
import com.acooly.core.utils.enums.Messageable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.google.common.collect.Maps;

/**
 * JQuery和JQuery-easyui作为前端的基础功能模板控制器
 * 
 * <p>
 * 说明： 使用JQuery和EasyUI作为前端的情况是JSTL和JSON开发模式的混合方式，该模式降低开发人员开发类EXTJS方式的前端的门槛。<br>
 * 规则：
 * <li>对于所有前导界面（index,create,edit等通过controller跳转到jsp使用JSTL初始化HTML界面）
 * 的訪問任然使用傳統JSTL页面跳转方式。
 * <li>对于数据访问动作（list分页查询，query条件查询，save,update,remove,get,show等）
 * 全部采用JSON方式实现前端UI功能操作免刷新
 * <li>数据导入导出任然使用传统方式
 * 
 * @author zhangpu
 * 
 * @param <T>
 * @param <M>
 */

public abstract class AbstractJQueryEntityController<T extends Entityable, M extends EntityService<T>>
		extends AbstractStandardEntityController<T, M> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractJQueryEntityController.class);

	/**
	 * EasyUI分页查询
	 */
	@RequestMapping(value = { "listJson" })
	@ResponseBody
	public JsonListResult<T> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<T> result = new JsonListResult<T>();
		allow(request, response, MappingMethod.list);
		try {
			result.appendData(referenceData(request));
			PageInfo<T> pageInfo = doList(request, response);
			result.setTotal(pageInfo.getTotalCount());
			result.setRows(pageInfo.getPageResults());
		} catch (Exception e) {
			handleException(result, "分页查询", e);
		}
		return result;
	}

	@RequestMapping(value = { "queryJson" })
	@ResponseBody
	public JsonListResult<T> queryJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<T> result = new JsonListResult<T>();
		allow(request, response, MappingMethod.query);
		try {
			result.appendData(referenceData(request));
			List<T> entities = doQuery(request, response);
			result.setTotal((long) entities.size());
			result.setRows(entities);
		} catch (Exception e) {
			handleException(result, "列表查询", e);
		}
		return result;
	}

	@RequestMapping(value = "saveJson")
	@ResponseBody
	public JsonEntityResult<T> saveJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<T> result = new JsonEntityResult<T>();
		allow(request, response, MappingMethod.create);
		try {
			result.setEntity(doSave(request, response, null, true));
			result.setMessage("新增成功");
		} catch (Exception e) {
			handleException(result, "新增", e);
		}
		return result;
	}

	@RequestMapping(value = "showJson")
	@ResponseBody
	public JsonEntityResult<T> showJson(HttpServletRequest request, HttpServletResponse response) {
		allow(request, response, MappingMethod.show);
		JsonEntityResult<T> result = new JsonEntityResult<T>();
		try {
			result.setEntity(loadEntity(request));
			result.setMessage("新增成功");
		} catch (Exception e) {
			handleException(result, "新增", e);
		}
		return result;
	}

	@RequestMapping(value = "updateJson")
	@ResponseBody
	public JsonEntityResult<T> updateJson(HttpServletRequest request, HttpServletResponse response) {
		allow(request, response, MappingMethod.update);
		JsonEntityResult<T> result = new JsonEntityResult<T>();
		try {
			result.setEntity(doSave(request, response, null, false));
			result.setMessage("更新成功");
		} catch (Exception e) {
			handleException(result, "更新", e);
		}
		return result;
	}

	@RequestMapping(value = "deleteJson")
	@ResponseBody
	public JsonResult deleteJson(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		allow(request, response, MappingMethod.delete);
		try {
			Serializable[] ids = getRequestIds(request);
			onRemove(request, response, null, ids);
			doRemove(request, response, null, ids);
			result.setMessage("删除成功");
		} catch (Exception e) {
			handleException(result, "删除", e);
		}
		return result;
	}

	@RequestMapping(value = { "importJson" })
	@ResponseBody
	public JsonResult importJson(HttpServletRequest request, HttpServletResponse response, Model model) {
		JsonResult result = new JsonResult();
		allow(request, response, MappingMethod.imports);
		try {
			List<T> entities = doImport(request, response);
			result.setMessage("导入成功，批量插入数据" + entities.size() + "条");
			entities = null;
		} catch (Exception e) {
			handleException(result, "Excel导入", e);
		}
		return result;
	}

	@Override
	protected PageInfo<T> getPageInfo(HttpServletRequest request) {
		PageInfo<T> pageinfo = new PageInfo<T>();
		pageinfo.setCountOfCurrentPage(getDefaultPageSize());
		String page = request.getParameter("page");
		if (StringUtils.isNotBlank(page)) {
			pageinfo.setCurrentPage(Integer.parseInt(page));
		}
		String rows = request.getParameter("rows");
		if (StringUtils.isNotBlank(rows)) {
			pageinfo.setCountOfCurrentPage(Integer.parseInt(rows));
		}
		return pageinfo;
	}

	@Override
	protected Map<String, Boolean> getSortMap(HttpServletRequest request) {
		Map<String, Boolean> sortMap = Maps.newHashMap();
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		if (StringUtils.isNotBlank(sort)) {
			sortMap.put(sort, "asc".equalsIgnoreCase(order));
		}
		return sortMap;
	}


	@Override
	public String getListView() {
		if (StringUtils.isNotBlank(listView)) {
			return listView;
		}
		return getRequestMapperValue();
	}

}
