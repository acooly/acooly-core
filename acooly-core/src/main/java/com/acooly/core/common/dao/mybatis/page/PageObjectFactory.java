package com.acooly.core.common.dao.mybatis.page;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import com.acooly.core.common.dao.support.PageInfo;

public class PageObjectFactory extends DefaultObjectFactory {

	/** UID */
	private static final long serialVersionUID = -1082960721558661578L;

	@Override
	public <T> T create(Class<T> type) {
		if (type == PageInfo.class) {
			return (T) new PageInfo();
		}
		return create(type, null, null);
	}

	@Override
	public <T> boolean isCollection(Class<T> type) {
		if (type == PageInfo.class) {
			return true;
		}
		return super.isCollection(type);
	}

}
