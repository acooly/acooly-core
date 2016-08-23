package com.acooly.core.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.GeneralException;

/**
 * 针对单个Entity对象的操作定义. 不依赖于具体ORM实现方案.
 * 
 * @author zhangpu
 */
public interface EntityService<T> {

	/**
	 * 根据ID获得单个对象
	 * 
	 * @param id
	 * @return
	 * @throws GeneralException
	 */
	T get(Serializable id) throws GeneralException;

	/**
	 * 获得对象所有集合
	 * 
	 * @return
	 * @throws GeneralException
	 */
	List<T> getAll() throws GeneralException;

	/**
	 * 保存对象
	 * 
	 * @param o
	 * @throws BusinessException
	 */
	void save(T t) throws BusinessException;

	/**
	 * 批量保存对象
	 * 
	 * @param o
	 * @throws BusinessException
	 */
	void saves(List<T> ts) throws BusinessException;
	
	/**
	 * 更新对象
	 * 
	 * @param t
	 * @throws BusinessException
	 */
	void update(T t) throws BusinessException;

	/**
	 * 删除对象
	 * 
	 * @param t
	 * @throws Exception
	 */
	void remove(T t) throws BusinessException;

	/**
	 * 根据ID删除对象
	 * 
	 * @param id
	 * @throws Exception
	 */
	void removeById(Serializable id) throws BusinessException;

	void removes(Serializable... ids) throws BusinessException;

	/**
	 * 带条件和排序的分页查询
	 * 
	 * @param pageInfo
	 * @param map
	 *            propertyName([EQ/LIKE...]_propertyName) --> value
	 * @param orderMap
	 *            propertyName --> true:asc,false:desc
	 * @return
	 * @throws GeneralException
	 */
	PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map,
			Map<String, Boolean> sortMap) throws GeneralException;

	PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map)
			throws GeneralException;

	List<T> query(Map<String, Object> map, Map<String, Boolean> sortMap);

}
