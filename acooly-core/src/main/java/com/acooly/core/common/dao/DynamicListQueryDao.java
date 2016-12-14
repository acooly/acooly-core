package com.acooly.core.common.dao;

import java.util.List;
import java.util.Map;

/**
 * 查询扩展DAO接口
 * 
 * @author zhangpu
 */
public interface DynamicListQueryDao<T> {

	/**
	 * 多条件支持排序动态查询, 返回查询全集，不分页，一般用于导出数据。
	 * 
	 * @param map
	 *            查詢條件
	 * @param orderMap
	 *            排序，多条件按Map迭代顺序组合.key:排序属性或字段 / value: true-ASC false:DESC
	 * @return 数据集合
	 */
	List<T> list(Map<String, Object> map, Map<String, Boolean> orderMap);

}
