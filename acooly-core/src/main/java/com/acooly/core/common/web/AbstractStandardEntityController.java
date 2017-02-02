package com.acooly.core.common.web;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.service.EntityService;

/**
 * 提供对JSTL/JSP作为前端的封装支持
 * <p>
 * <li>错误处理，消息回传通过临时Session变量中转
 * <li>通过跳转新页面方式显示处理结果和数据。
 * <li>对基础功能进行模板方法封装（CRUD,分页查询，直接查询，导入导出），直接可用於前端調用。
 * 
 * @author zhangpu
 * 
 * @param <T>
 * @param <M>
 */
public abstract class AbstractStandardEntityController<T extends AbstractEntity, M extends EntityService<T>>
		extends AbstractFileOperationController<T, M> {

	/** 系统日志 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractStandardEntityController.class);
	protected static final String LISTVIEW_POSIX = "List";
	protected static final String EDITVIEW_POSIX = "Edit";
	protected static final String SHOWVIEW_POSIX = "Show";
	protected static final String IMPVIEW_POSIX = "Import";
	/** 导入视图 */
	protected String importView;
	protected static final String ACTION_CREATE = "create";
	protected static final String ACTION_EDIT = "edit";

	protected String springMvcPostfix = ".html";
	protected String requestMapperValue;

	/** list跳转页面 */
	protected String listView;

	/** 编辑跳转页面 */
	protected String editView;

	/** 查看跳转页面 */
	protected String showView;

	/** 成功跳转页面 */
	protected String successView;

	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			model.addAllAttributes(referenceData(request));
		} catch (Exception e) {
			logger.warn(getExceptionMessage("index", e), e);
			handleException("主界面", e, request);
		}
		return getListView();
	}

	/**
	 * 默认多条件组合分页查询<br>
	 * 
	 * <li>多条件组合支持
	 * <li>支持多字段排序
	 * <li>支持分页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
		allow(request, response, MappingMethod.list);
		try {
			model.addAllAttributes(referenceData(request));
			PageInfo<T> pageInfo = doList(request, response, model);
			model.addAttribute(getEntityListName(), pageInfo);
		} catch (Exception e) {
			logger.warn(getExceptionMessage("list", e), e);
			handleException("分页查询", e, request);
		}
		return getListView();
	}

	@RequestMapping(value = { "query" })
	public String query(HttpServletRequest request, HttpServletResponse response, Model model) {
		allow(request, response, MappingMethod.query);
		try {
			model.addAllAttributes(referenceData(request));
			model.addAttribute(getEntityListName(), doQuery(request, response, model));
		} catch (Exception e) {
			logger.warn(getExceptionMessage("query", e), e);
			handleException("列表查询", e, request);
		}
		return getListView();
	}

	@RequestMapping(value = { "exportXls", "exportExcel" })
	public String exportExcel(HttpServletRequest request, HttpServletResponse response, Model model) {
		allow(request, response, MappingMethod.exports);
		try {
			model.addAllAttributes(referenceData(request));
			doExportExcel(request, response);
			return null;
		} catch (Exception e) {
			logger.warn(getExceptionMessage("exportExcel", e), e);
			handleException("导出Excel", e, request);
		}
		return getSuccessView();
	}

	@RequestMapping(value = "exportCsv")
	public String exportCsv(Model model, HttpServletRequest request, HttpServletResponse response) {
		allow(request, response, MappingMethod.exports);
		try {
			model.addAllAttributes(referenceData(request));
			doExportCsv(request, response);
			return null;
		} catch (Exception e) {
			logger.warn(getExceptionMessage("exportCsv", e), e);
			handleException("导出CVS", e, request);
		}
		return getSuccessView();
	}

	@RequestMapping(value = { "importView" })
	public String importView(Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			model.addAllAttributes(referenceData(request));
		} catch (Exception e) {
			logger.warn(getExceptionMessage("importView", e), e);
			handleException("导入界面", e, request);
		}
		return getImportView();
	}

	@RequestMapping(value = { "importXls", "importExcel" })
	public String importXls(Model model, HttpServletRequest request, HttpServletResponse response) {
		allow(request, response, MappingMethod.imports);
		try {
			List<T> entities = doImport(request, response, FileType.EXCEL);
			saveMessage(request, "Excel导入成功，批量插入数据" + entities.size() + "条");
		} catch (Exception e) {
			logger.warn(getExceptionMessage("importXls", e), e);
			handleException("Excel导入", e, request);
		}
		return getSuccessView();
	}

	@RequestMapping(value = "importCsv")
	public String importCsv(Model model, HttpServletRequest request, HttpServletResponse response) {
		allow(request, response, MappingMethod.imports);
		try {
			List<T> entities = doImport(request, response, FileType.CSV);
			saveMessage(request, "CSV导入成功，批量插入数据" + entities.size() + "条");
		} catch (Exception e) {
			logger.warn(getExceptionMessage("importCsv", e), e);
			handleException("CSV导入", e, request);
		}
		return getSuccessView();
	}

	@RequestMapping(value = { "show", "get" })
	public String show(HttpServletRequest request, HttpServletResponse response, Model model) {
		allow(request, response, MappingMethod.show);
		try {
			model.addAllAttributes(referenceData(request));
			T entity = loadEntity(request);
			if (entity == null) {
				throw new RuntimeException("LoadEntity failure.");
			}
			onShow(request, response, model, entity);
			model.addAttribute(getEntityName(), entity);
		} catch (Exception e) {
			logger.warn(getExceptionMessage("show", e), e);
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
	public String create(HttpServletRequest request, HttpServletResponse response, Model model) {
		allow(request, response, MappingMethod.create);
		try {
			model.addAllAttributes(referenceData(request));
			onCreate(request, response, model);
			model.addAttribute("action", ACTION_CREATE);
		} catch (Exception e) {
			logger.warn(getExceptionMessage("create", e), e);
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
	public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
		allow(request, response, MappingMethod.update);
		try {
			model.addAllAttributes(referenceData(request));
			T entity = loadEntity(request);
			model.addAttribute("action", ACTION_EDIT);
			model.addAttribute(getEntityName(), entity);
			onEdit(request, response, model, entity);
		} catch (Exception e) {
			logger.warn(getExceptionMessage("edit", e), e);
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
	public String save(HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		String redirectUrl = getSuccessView();
		try {
			model.addAllAttributes(referenceData(request));
			doSave(request, response, model, true);
			saveMessage(request, "保存成功");
		} catch (Exception e) {
			logger.warn(getExceptionMessage("save", e), e);
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
	public String update(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
			HttpServletResponse response) {
		allow(request, response, MappingMethod.update);
		String redirectUrl = getSuccessView();
		T entity = null;
		try {
			model.addAllAttributes(referenceData(request));
			doSave(request, response, model, false);
			saveMessage(request, "更新成功");
		} catch (Exception e) {
			logger.warn(getExceptionMessage("update", e), e);
			handleException("更新", e, request);
			redirectUrl = "redirect:" + getUpdateFailureRedirectUrl(entity);
		}
		return redirectUrl;
	}

	/**
	 * 删除实体,支持单个和批量
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "remove")
	public String remove(HttpServletRequest request, HttpServletResponse response, Model model) {
		allow(request, response, MappingMethod.delete);
		try {
			model.addAllAttributes(referenceData(request));
			Serializable[] ids = getRequestIds(request);
			onRemove(request, response, model, ids);
			doRemove(request, response, model, ids);
			saveMessage(request, "删除成功");
		} catch (Exception e) {
			logger.warn(getExceptionMessage("remove", e), e);
			handleException("删除", e, request);
		}
		return getSuccessView();
	}

	protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {

	}

	protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, T entity) {

	}

	protected void onShow(HttpServletRequest request, HttpServletResponse response, Model model, T entity)
			throws Exception {
	}

	protected void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
			throws Exception {
	}

	/**
	 * 向View层传递message时将message放入httpSession的messages变量中.
	 * 放在session中能保证message即使Redirect也不会消失。 需配合
	 * MessageFilter}使用, MessageFilter实现从Session中读取message并放入到request中
	 * 然后，从session中删除message
	 */
	@SuppressWarnings("unchecked")
	protected void saveMessage(HttpServletRequest request, String message) {
		if (StringUtils.isNotBlank(message)) {
			List<Object> messages = (List<Object>) WebUtils.getOrCreateSessionAttribute(request.getSession(),
					"messages", ArrayList.class);
			messages.add(message);
		}
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
		return getRequestMapperValue() + "/edit" + springMvcPostfix + "?id=" + entity.getId();
	}

	protected String getRequestMapperValue() {
		if (StringUtils.isBlank(requestMapperValue)) {
			String[] value = (String[]) AnnotationUtils.getValue(getClass().getAnnotation(RequestMapping.class));
			requestMapperValue = value != null && value.length > 0 ? value[0] : "";
		}
		return requestMapperValue;
	}

	protected void handleException(String action, Exception e, HttpServletRequest request) {
		String message = getExceptionMessage(action, e);
		saveMessage(request, message);
		logger.error(message,e);
	}

	protected String getExceptionMessage(String action, Exception e) {
		Throwable source = getSqlException(e);
		return (StringUtils.isNotBlank(action) ? (action + " -> ") : "") + e.getMessage()
				+ (source != null ? " --> DB_ERROR:" + source.getMessage() : "");
	}

	protected Throwable getSqlException(Exception e) {
		Throwable source = e;
		// 简单查询异常的8层Cause
		for (int i = 0; i < 8; i++) {
			if (source.getCause() == null) {
				return null;
			} else {
				source = source.getCause();
			}
			if (source.getClass().toString().equals(SQLException.class.toString())) {
				return source.getCause();
			}
		}
		return source;
	}

	public String getListView() {
		if (StringUtils.isNotBlank(listView)) {
			return listView;
		}
		return getRequestMapperValue() + LISTVIEW_POSIX;
	}

	public String getImportView() {
		if (StringUtils.isNotBlank(importView)) {
			return importView;
		}
		return getRequestMapperValue() + IMPVIEW_POSIX;
	}

	public void setImportView(String importView) {
		this.importView = importView;
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
		return "redirect:" + getRequestMapperValue() + "/index" + getSpringMvcPostfix();
	}

	public String getSpringMvcPostfix() {
		return springMvcPostfix;
	}

	public void setSpringMvcPostfix(String springMvcPostfix) {
		this.springMvcPostfix = springMvcPostfix;
	}

}
