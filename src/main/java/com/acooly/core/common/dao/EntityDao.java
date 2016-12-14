package com.acooly.core.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.acooly.core.common.dao.support.PageInfo;

/**
 * 针对单个Entity对象的操作,不依赖于具体ORM实现方案.
 * 
 * @author zhangpu
 */
public interface EntityDao<T> {
	
	/**
	 * 创建新实体
	 * 
	 * @param o
	 */
	void create(T o);
	
	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	T get(Serializable id);
	
	/**
	 * 更新
	 * 
	 * @param o
	 */
	void update(T o);

	
	void flush();
	
	/**
	 * 批量保存和更新
	 * 
	 * @param entities
	 */
	void saves(List<T> entities);
	
	/**
	 * 删除
	 * 
	 * @param o
	 */
	void remove(T o);
	
	/**
	 * 根据ID删除
	 * 
	 * @param id
	 */
	void removeById(Serializable id);
	
	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	void removes(Serializable... ids);
	
	/**
	 * 获取所有
	 * 
	 * @return
	 */
	List<T> getAll();
	
	/**
	 * 根据指定列和值查询
	 * 
	 * @param property 关系操作符_属性名,如：EQ_name
	 * @param value 值
	 * @return
	 */
	List<T> find(String property, Object value);
	
	/**
	 * 根据指定属性名和值查询唯一数据
	 * 
	 * @param property
	 * @param value
	 * @return
	 */
	T findUniqu(String property, Object value);
	
	/**
	 * 分页条件排序查询
	 * 
	 * @param pageInfo
	 * @param map
	 * @param sortMap
	 * @return
	 */
	PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map, Map<String, Boolean> sortMap);
	
	/**
	 * 条件排序查询
	 * 
	 * @param map
	 * @param sortMap
	 * @return
	 */
	List<T> list(Map<String, Object> map, Map<String, Boolean> sortMap);
	
}
