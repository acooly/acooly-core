package com.acooly.module.mybatis.page;

import com.acooly.core.common.dao.support.PageInfo;
import com.google.common.collect.Lists;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

import java.util.List;

public class PageObjectWrapper implements ObjectWrapper {

	private PageInfo pageInfo;

	@Override
	public Object get(PropertyTokenizer prop) {
		return null;
	}

	public PageObjectWrapper(PageInfo pageInfo) {
		super();
		this.pageInfo = pageInfo;
	}

	@Override
	public <E> void addAll(List<E> element) {
		MyBatisPage page = (MyBatisPage) element;
		pageInfo.setTotalCount(page.getTotalCount());
		pageInfo.setTotalPage(page.getTotalPage());
		pageInfo.setCountOfCurrentPage(page.getCountOfCurrentPage());
		pageInfo.setCurrentPage(page.getCurrentPage());
		pageInfo.setPageResults(Lists.newArrayList(page));
	}

	@Override
	public void set(PropertyTokenizer prop, Object value) {

	}

	@Override
	public String findProperty(String name, boolean useCamelCaseMapping) {
		return null;
	}

	@Override
	public String[] getGetterNames() {
		return new String[0];
	}

	@Override
	public String[] getSetterNames() {
		return new String[0];
	}

	@Override
	public Class<?> getSetterType(String name) {
		return null;
	}

	@Override
	public Class<?> getGetterType(String name) {
		return null;
	}

	@Override
	public boolean hasSetter(String name) {
		return false;
	}

	@Override
	public boolean hasGetter(String name) {
		return false;
	}

	@Override
	public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
		return null;
	}

	@Override
	public boolean isCollection() {
		return false;
	}

	@Override
	public void add(Object element) {

	}

}
