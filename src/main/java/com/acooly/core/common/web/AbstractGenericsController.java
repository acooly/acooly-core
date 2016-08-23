package com.acooly.core.common.web;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.exception.UnMappingMethodException;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.GenericsUtils;
import com.acooly.core.utils.system.IPUtil;

/**
 * 抽象泛型控制器
 * 
 * 提供根据泛型获取子类的受管实体和实体服务的信息，包括名称，类名和实例等。
 * 
 * @author zhangpu
 * 
 * @param <T>
 * @param <M>
 */
public abstract class AbstractGenericsController<T extends AbstractEntity, M extends EntityService<T>>
		extends MultiActionController {

	private static final Logger logger = LoggerFactory.getLogger(AbstractGenericsController.class);

	/** 所管理的Entity类型. */
	protected Class<T> entityClass;
	/** 管理Entity所用的 Service */
	private M entityService;

	protected String allowMapping = "*";

	protected void allow(HttpServletRequest request, HttpServletResponse response, MappingMethod mappingMethod) {
		if (!MappingMethod.allow(allowMapping, mappingMethod)) {
			logger.warn("不支持的自动Mapping方法：{}, IP:{}", mappingMethod, IPUtil.getIpAddr(request));
			throw new UnMappingMethodException();
		}
	}

	/** 基于@ExceptionHandler异常处理 */
	@ExceptionHandler(UnMappingMethodException.class)
	public String allowExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			response.sendError(404);
		} catch (Exception e) {
		}
		return null;
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
		return StringUtils.uncapitalize(ClassUtils.getShortName(getEntityClass()));
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
			List<Field> fields = BeanUtils.getFieldsByType(this, GenericsUtils.getSuperClassGenricType(getClass(), 1));
			try {
				entityService = (M) BeanUtils.getDeclaredProperty(this, fields.get(0).getName());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			Assert.notNull(entityService, "EntityService未能成功初始化");
		}
		return entityService;
	}

}
