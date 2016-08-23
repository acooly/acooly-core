package com.acooly.core.common.dao;

import java.util.Map;

import com.acooly.core.common.dao.support.PageInfo;

/**
 * 查询扩展DAO接口
 * 
 * @author zhangpu
 */
public interface DynamicPagedQueryDao<T> {

	/**
	 * 默认分页查询接口 多条件分页动态查询抽象方 法，支持排序
	 * 扩展层直接封装调用的接口，这里可以扩展实现HQL,SQL等方式方式查询,作为JPA查询的扩展
	 * 
	 * @param pageInfo
	 *            分页对象，@see com.acooly.common.dao.support.PageInfo
	 * @param map
	 *            查询条件
	 * @param orderMap
	 *            排序，多条件按Map迭代顺序组合.key:排序属性或字段 / value: true-ASC false:DESC
	 * @return 分页对象
	 */
	PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap);

}
