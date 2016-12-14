package com.acooly.core.common.web;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Servlets;
import com.google.common.collect.Maps;

/**
 * 实体抽象控制口封装
 * 
 * 提供CRUD,分页查询，导入，导出，上传等常用功能的默认实现
 * 
 * @author zhangpu
 * 
 * @param <T>
 * @param <M>
 */

public class AbstractCrudController<T extends AbstractEntity, M extends EntityService<T>>
		extends AbstractGenericsController<T, M> {

	/** 系统日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractCrudController.class);
	protected static final String LISTVIEW_POSIX = "List";
	protected static final String EDITVIEW_POSIX = "Edit";
	protected static final String SHOWVIEW_POSIX = "Show";
	protected static final String ACTION_CREATE = "create";
	protected static final String ACTION_EDIT = "edit";

	protected String springMvcPostfix = ".mvc";
	protected String requestMapperValue;

	/** list跳转页面 */
	protected String listView;

	/** 编辑跳转页面 */
	protected String editView;

	/** 查看跳转页面 */
	protected String showView;

	/** 成功跳转页面 */
	protected String successView;

	/**
	 * 功能模块首页 可选用于进入自定义的功能界面首页 可选用于进入首次进入不执行默认查询的list
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "index" })
	public String index(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		return list(model, request, response);
	}

	/**
	 * 默认多条件组合分页查询<br>
	 * 
	 * <li>多条件组合支持 <li>支持多字段排序 <li>支持分页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "list" })
	public String list(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			model.addAllAttributes(referenceData(request));
			onList(model, request, response);
		} catch (Exception e) {
			handleException("列表查询", e, request);
		}
		return getListView();
	}

	@RequestMapping(value = { "show", "get" })
	public String show(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			model.addAllAttributes(referenceData(request));
			T entity = loadEntity(request);
			if (entity == null) {
				throw new RuntimeException("LoadEntity failure.");
			}
			onShow(entity, model, request, response);
			model.addAttribute(getEntityName(), entity);
		} catch (Exception e) {
			handleException("查看", e, request);
		}
		return getShowView();
	}

	/**
	 * 创建界面导航,初始化创建界面数据
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "create")
	public String create(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			model.addAllAttributes(referenceData(request));
			onCreate(model, request, response);
			model.addAttribute("action", ACTION_CREATE);
			model.addAttribute(getEntityName(), getEntityClass().newInstance());
		} catch (Exception e) {
			handleException("新建", e, request);
		}
		return getEditView();
	}

	/**
	 * 创建编辑导航,初始化创建界面数据
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "edit")
	public String edit(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			model.addAllAttributes(referenceData(request));
			T entity = loadEntity(request);
			model.addAttribute("action", ACTION_EDIT);
			model.addAttribute(getEntityName(), entity);
			onEdit(entity, model, request, response);
		} catch (Exception e) {
			handleException("编辑", e, request);
		}
		return getEditView();
	}

	/**
	 * 新增保存实体模板
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		String redirectUrl = getSuccessView();
		try {
			model.addAllAttributes(referenceData(request));
			T entity = getEntityClass().newInstance();
			doDataBinding(request, entity);
			onSave(entity, model, request, response, true);
			saveMessage(request, "保存成功");
		} catch (Exception e) {
			handleException("保存", e, request);
			redirectUrl = "redirect:" + getSaveFailureRedirectUrl();
		}
		return redirectUrl;
	}

	/**
	 * 更新保存实体模板
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "update")
	public String update(Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		String redirectUrl = getSuccessView();
		T entity = null;
		try {
			model.addAllAttributes(referenceData(request));
			entity = loadEntity(request);
			doDataBinding(request, entity);
			onSave(entity, model, request, response, false);
			saveMessage(request, "更新成功");
		} catch (Exception e) {
			handleException("更新", e, request);
			redirectUrl = "redirect:" + getUpdateFailureRedirectUrl(entity);
		}
		return redirectUrl;
	}

	/**
	 * 根据实体ID删除单个实体
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "remove")
	public String remove(@RequestParam("id") Long id, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			model.addAllAttributes(referenceData(request));
			onRemove(id, model, request, response);
			saveMessage(request, "删除成功");
		} catch (Exception e) {
			handleException("删除", e, request);
		}
		return getSuccessView();
	}

	/**
	 * 根据多个实体ID批量删除实体
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "removes")
	public String removes(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			model.addAllAttributes(referenceData(request));
			String[] id = request.getParameterValues("id");
			Long[] lid = new Long[id.length];
			for (int i = 0; i < id.length; i++) {
				lid[i] = Long.valueOf(id[i]);
			}
			onRemoves(lid, model, request, response);
			saveMessage(request, "批量删除成功");
		} catch (Exception e) {
			handleException("批量删除", e, request);
		}
		return getSuccessView();
	}

	protected void onRemoves(Serializable[] ids, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		getEntityService().removes(ids);
	}

	protected void onRemove(Long id, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		getEntityService().removeById(id);
	}

	/**
	 * 列表查询的模板方法
	 * 
	 * @param model
	 * @param request
	 * @param response
	 */
	protected void onList(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageInfo<T> pageinfo = getPageInfo(request);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		Map<String, Boolean> sortMap = getSortMap(request);
		getEntityService().query(pageinfo, searchParams, sortMap);
		model.addAttribute(getEntityListName(), pageinfo.getPageResults());
		request.setAttribute("totalRows",
				Integer.valueOf(String.valueOf(pageinfo.getTotalCount())));
	}

	protected List<T> onQuery(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		Map<String, Boolean> sortMap = getSortMap(request);
		return getEntityService().query(searchParams, sortMap);
	}

	protected void onEdit(T entity, Model model, HttpServletRequest request,
			HttpServletResponse response) {

	}

	protected void onCreate(Model model, HttpServletRequest request,
			HttpServletResponse response) {

	}

	protected void onShow(T entity, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

	}

	/**
	 * 在保存实体前，对实体进行加工处理。
	 * 
	 * @param entity
	 * @param model
	 * @param request
	 * @param response
	 * @param isCreate
	 * @return
	 */
	protected void onSave(T entity, Model model, HttpServletRequest request,
			HttpServletResponse response, boolean isCreate) {
		if (isCreate) {

		} else {
			// 修改entity对象前的准备操作
		}
		// 这里服务层默认是根据entity的Id是否为空自动判断是SAVE还是UPDATE.
		getEntityService().save(entity);
	}

	protected void doDataBinding(HttpServletRequest request, Object command)
			throws Exception {
		bind(request, command);
	}

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

	protected int getDefaultPageSize() {
		return 15;
	}

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

	protected Map<String, Object> referenceData(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		referenceData(request, map);
		/** ec_table default setting start */
		String ecp = request.getParameter("ec_p");
		if (StringUtils.isNotBlank(ecp)) {
			request.setAttribute("ec_p", ecp);
		}
		request.setAttribute("totalRows", 0);
		/** ec_table default setting end */
		return map;
	}

	/**
	 * 向View层传递message时将message放入httpSession的messages变量中.
	 * 放在session中能保证message即使Redirect也不会消失。 需配合
	 * {@link com.acooly.core.common.web.support.framwork.common.web.acooly.common.util.filer.MessageFilter
	 * MessageFilter}使用, MessageFilter实现从Session中读取message并放入到request中
	 * 然后，从session中删除message
	 */
	@SuppressWarnings("unchecked")
	protected void saveMessage(HttpServletRequest request, String message) {
		if (StringUtils.isNotBlank(message)) {
			List<Object> messages = (List<Object>) WebUtils
					.getOrCreateSessionAttribute(request.getSession(),
							"messages", ArrayList.class);
			messages.add(message);
		}
	}

	/**
	 * 根据Request中的id参数从数据库LOAD实体对象
	 * 
	 * @param id
	 * @return
	 */
	protected T loadEntity(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (id != null) {
			try {
				return getEntityService().get(Long.valueOf(id));
			} catch (Exception e) {
				logger.warn("loadEntity -> " + e.getMessage(), e);
				throw new RuntimeException(e);
			}

		}
		return null;
	}

	/**
	 * 创建Save实体对象失败的重定向地址
	 * 
	 * @return
	 */
	protected String getSaveFailureRedirectUrl() {
		return getRequestMapperValue() + "/create" + springMvcPostfix;
	}

	protected String getUpdateFailureRedirectUrl(T entity) {
		return getRequestMapperValue() + "/edit" + springMvcPostfix + "?id="
				+ entity.getId();
	}

	protected void referenceData(HttpServletRequest request,
			Map<String, Object> model) {

	}

	protected String getRequestMapperValue() {
		if (StringUtils.isBlank(requestMapperValue)) {
			String[] value = (String[]) AnnotationUtils.getValue(getClass()
					.getAnnotation(RequestMapping.class));
			requestMapperValue = value != null && value.length > 0 ? value[0]
					: "";
		}
		return requestMapperValue;
	}

	/**
	 * 兼容设置
	 */
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		initBinder(binder);
	}

	protected void handleException(String action, Exception e,
			HttpServletRequest request) {
		Throwable source = getSqlException(e);
		saveMessage(
				request,
				action
						+ " -> "
						+ e.getMessage()
						+ (source != null ? " --> DB_ERROR:"
								+ source.getMessage() : ""));
		logger.warn(action
				+ "-> "
				+ e.getMessage()
				+ (source != null ? " --> DB_ERROR:" + source.getMessage() : ""));
	}

	protected Throwable getSqlException(Exception e) {
		Throwable source = e;
		// 简单查询异常的8层Cause
		for (int i = 0; i < 8; i++) {
			if (source.getCause().getClass().toString()
					.equals(SQLException.class.toString())) {
				return source.getCause();
			}
			source = source.getCause();
		}
		return null;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Dates.CHINESE_DATE_FORMAT_LINE);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(
				Integer.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(
				Double.class, true));
	}

	public String getListView() {
		if (StringUtils.isNotBlank(listView)) {
			return listView;
		}
		return getRequestMapperValue() + LISTVIEW_POSIX;
	}

	public String getEditView() {
		if (StringUtils.isNotBlank(editView)) {
			return editView;
		}
		return getRequestMapperValue() + EDITVIEW_POSIX;
	}

	public String getShowView() {
		if (StringUtils.isNotBlank(showView)) {
			return showView;
		}
		return getRequestMapperValue() + SHOWVIEW_POSIX;
	}

	public String getSuccessView() {
		if (StringUtils.isNotBlank(successView)) {
			return successView;
		}
		return "redirect:" + getRequestMapperValue() + "/index.mvc";
	}

	public String getSpringMvcPostfix() {
		return springMvcPostfix;
	}

	public void setSpringMvcPostfix(String springMvcPostfix) {
		this.springMvcPostfix = springMvcPostfix;
	}

}
