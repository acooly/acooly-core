package com.acooly.core.common.dao.jpa;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 动态查询 条件
 * 
 * @author zhangpu
 * @date 2012年6月30日
 */
public class SearchFilter {
	
	public enum Operator {
		EQ, NEQ, LIKE, LLIKE, RLIKE, GT, LT, GTE, LTE, IN, NOTIN, NULL, NOTNULL,
	}
	
	public String fieldName;
	public Object value;
	public Operator operator;
	
	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}
	
	public static List<SearchFilter> parse(Map<String, Object> searchParams) {
		List<SearchFilter> filters = Lists.newArrayList();
		for (Entry<String, Object> entry : searchParams.entrySet()) {
			SearchFilter searchFilter = parse(entry.getKey(), entry.getValue());
			if (searchFilter != null) {
				filters.add(searchFilter);
			}
		}
		return filters;
	}
	
	public static SearchFilter parse(String param, Object value) {
		Assert.hasText(param);
		String[] names = StringUtils.split(param, "_");
		if (names.length != 2) {
			throw new IllegalArgumentException(param + " is not a valid search filter name");
		}
		return new SearchFilter(names[1], Operator.valueOf(names[0].toUpperCase()), value);
		
	}
}
