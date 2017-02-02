package com.acooly.core.common.web;

import java.io.BufferedOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.limit.Limit;
import org.extremecomponents.table.limit.LimitFactory;
import org.extremecomponents.table.limit.TableLimit;
import org.extremecomponents.table.limit.TableLimitFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
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
import com.acooly.core.common.web.support.Servlets;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.GenericsUtils;
import com.google.common.collect.Maps;

/**
 * 基础控制器
 * 
 * @deprecated
 * @author zhangpu
 * 
 * @param <T>
 * @param <M>
 */
public abstract class AbstractController<T extends AbstractEntity, M extends EntityService<T>> {

	/** 系统日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractController.class);
	@Autowired(required = false)
	private Validator[] validators;
	private String springMvcPostfix = ".mvc";

	/** 所管理的Entity类型. */
	protected Class<T> entityClass;
	/** 管理Entity所用的 Service */
	private M entityService;

	private String requestMapperValue;

	private static final String LISTVIEW_POSIX = "List";
	private static final String EXPXLSVIEW_POSIX = "Xls";
	private static final String EDITVIEW_POSIX = "Edit";
	private static final String SHOWVIEW_POSIX = "Show";
	private static final String ACTION_CREATE = "create";
	private static final String ACTION_EDIT = "edit";

	/** list跳转页面 */
	protected String listView;

	/** 导出ExcelView */
	protected String expXlsView;

	/** 编辑跳转页面 */
	protected String editView;

	/** 查看跳转页面 */
	protected String showView;

	/** 成功跳转页面 */
	protected String successView;

	/**
	 * 默认多条件组合分页查询<br>
	 * 
	 * <li>多条件组合支持 <li>支持多字段排序 <li>支持分页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "index", "list" })
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

	/**
	 * 导出Excel模板方法
	 * 
	 * 本模板方法提供EXCEL导出的程序框架，子类需要实现doExportXls：根据业务要求，编码选择输出操作内容和方式。
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "exportXls")
	public String exportXls(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String returnUrl = getExpXlsView();
		try {
			model.addAllAttributes(referenceData(request));
			List<T> list = onQuery(model, request, response);
			settingExportExcelHeader(response);
			// 如果不需要返回到导出页面(如果使用jxl或POI方式)，这返回false.
			boolean needResponseToExportViewPage = doExportXls(list, request,
					response);
			if (!needResponseToExportViewPage) {
				return null;
			}
			// 如果是需要通过JSP页面导出，则把数据传入页面端
			model.addAttribute(getEntityListName(), list);
		} catch (Exception e) {
			handleException("导出Excel", e, request);
			returnUrl = getSuccessView();
		}
		return returnUrl;
	}

	/**
	 * 导出CSV模板方法
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "exportCsv")
	public String exportCsv(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String returnUrl = null;
		try {
			model.addAllAttributes(referenceData(request));
			List<T> list = onQuery(model, request, response);
			settingExportCsvHeader(response);
			onExportCsv(list, request, response);
			return null;
		} catch (Exception e) {
			handleException("导出CSV", e, request);
			returnUrl = getSuccessView();
		}
		return returnUrl;
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
			handleException("创建", e, request);
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
			saveMessage(request, "保存成功");
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
			saveMessage(request, "删除成功");
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

	/**
	 * 实现导出输出的程序模板，子类可以直接使用或根据自己的需求覆写
	 * 
	 * @param list
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void onExportCsv(List<T> list, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(response.getOutputStream());
			doExportCsv(list, out);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			IOUtils.closeQuietly(out);
		}
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

	/**
	 * 导出Excel实现。 <br>
	 * <li>可选使用jxl,poi方式直接输出流到reponse,不需要导出页面。该方法返回fase; <li>
	 * 可选使用页面方式实现，需要返回到导出页面。该方法返回true
	 * 
	 * @param list
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean doExportXls(List<T> list, HttpServletRequest request,
			HttpServletResponse response) {
		return true;
	}

	protected void doDataBinding(HttpServletRequest request, Object command)
			throws Exception {
		bind(request, command);
	}

	private void bind(HttpServletRequest request, Object command) throws Exception {
		ServletRequestDataBinder binder = createBinder(request, command);
		binder.bind(request);
		if (this.validators != null) {
			for (Validator validator : this.validators) {
				if (validator.supports(command.getClass())) {
					ValidationUtils.invokeValidator(validator, command, binder.getBindingResult());
				}
			}
		}
		binder.closeNoCatch();
	}
	protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command, "command");
		initBinder(request, binder);
		return binder;
	}


	/**
	 * 根据业务需求，把需要输出的结果输出到输出流中,由子类实现
	 * 
	 * @param list
	 * @param out
	 * @throws Exception
	 */
	protected void doExportCsv(List<T> list, BufferedOutputStream out)
			throws Exception {

	}

	/**
	 * 设置导出Excel文件的Http头，只要是设置下载文件的文件名和MITA信息
	 */
	protected void settingExportCsvHeader(HttpServletResponse response) {
		String fileName = getExportFileName();
		try {
			fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		} catch (Exception e) {
			// ig
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ fileName + ".cvs\"");
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
		return 10;
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

	/**
	 * 导出文件的文件名
	 * 
	 * @return
	 */
	protected String getExportFileName() {
		return getEntityName();
	}

	/**
	 * 设置导出Excel文件的Http头，只要是设置下载文件的文件名和MITA信息
	 */
	protected void settingExportExcelHeader(HttpServletResponse response) {
		String fileName = getExportFileName();
		try {
			fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		} catch (Exception e) {
			// ig
		}
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment");
		response.setHeader("Content-Disposition", "filename=\"" + fileName
				+ ".xls\"");
	}

	/**
	 * 兼容设置
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		initBinder(binder);
	}

	/**
	 * 取得entityClass的函数
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
		return entityClass;
	}

	/** 获取所管理的对象名.首字母小写，如"user" */
	protected String getEntityName() {
		return StringUtils.uncapitalize(ClassUtils
				.getShortName(getEntityClass()));
	}

	/** 获取所管理的对象名.首字母小写，如"user" */
	protected String getEntityListName() {
		return getEntityName() + "s";
	}

	/**
	 * 获得EntityManager类进行CRUD操作，可以在子类重载.
	 */
	@SuppressWarnings("unchecked")
	protected M getEntityService() {
		if (entityService == null) {
			List<Field> fields = BeanUtils.getFieldsByType(this,
					GenericsUtils.getSuperClassGenricType(getClass(), 1));
			try {
				entityService = (M) BeanUtils.getDeclaredProperty(this, fields
						.get(0).getName());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			Assert.notNull(entityService, "EntityService未能成功初始化");
		}
		return entityService;
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

	public String getExpXlsView() {
		if (StringUtils.isNotBlank(expXlsView)) {
			return expXlsView;
		}
		return getRequestMapperValue() + EXPXLSVIEW_POSIX;
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
