package com.acooly.core.common.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;

import com.acooly.core.common.dao.EntityDao;
import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.GeneralException;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.GenericsUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础EntityService的抽象实现。
 * 
 * @author zhangpu
 * 
 * @param <T>
 *            被管理的实体类
 * @param <M>
 *            实体类的DAO
 */
@Transactional
public abstract class EntityServiceImpl<T, M extends EntityDao<T>> implements
		ApplicationContextAware, EntityService<T> {

	private M entityDao;
	private ApplicationContext context;

	@SuppressWarnings("unchecked")
	protected M getEntityDao() throws BusinessException {
		if (entityDao != null) {
			return entityDao;
		}
		// 获取定义的第一个实例变量类型
		Class<M> daoType = GenericsUtils.getSuperClassGenricType(getClass(), 1);
		List<Field> fields = BeanUtils.getFieldsByType(this, daoType);
		try {
			if (fields != null && fields.size() > 0) {
				entityDao = (M) BeanUtils.getDeclaredProperty(this,
						fields.get(0).getName());
			} else {
				entityDao = (M) context.getBean(daoType);
			}
		} catch (IllegalAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		} catch (NoSuchFieldException e) {
			throw new BusinessException(e.getMessage(),e);
		}
		return entityDao;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public T get(Serializable id) throws BusinessException {
		try {
			return getEntityDao().get(id);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}

	}

	@Override
	public List<T> getAll() throws BusinessException {
		try {
			return getEntityDao().getAll();
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

	@Override
	public void remove(T o) throws BusinessException {
		try {
			getEntityDao().remove(o);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

	@Override
	public void removeById(Serializable id) throws BusinessException {
		try {
			getEntityDao().removeById(id);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}

	}

	@Override
	public void removes(Serializable... ids) throws BusinessException {
		try {
			getEntityDao().removes(ids);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

	@Override
	public void save(T o) throws BusinessException {
		try {
			getEntityDao().create(o);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

	@Override
	public void saves(List<T> ts) throws BusinessException {
		try {
			getEntityDao().saves(ts);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

	@Override
	public void update(T o) throws BusinessException {
		try {
			getEntityDao().update(o);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

	@Override
	public PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map,
			Map<String, Boolean> orderMap) throws GeneralException {
		try {
			return getEntityDao().query(pageInfo, map, orderMap);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

	@Override
	public PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map)
			throws GeneralException {
		return query(pageInfo, map, null);
	}

	@Override
	public List<T> query(Map<String, Object> map, Map<String, Boolean> sortMap) {
		try {
			return getEntityDao().list(map, sortMap);
		} catch (DataAccessException e) {
			throw new BusinessException(e.getMessage(),e);
		}
	}

}
