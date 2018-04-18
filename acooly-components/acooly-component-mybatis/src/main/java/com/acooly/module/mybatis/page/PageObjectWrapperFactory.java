package com.acooly.module.mybatis.page;

import com.acooly.core.common.dao.support.PageInfo;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

/**
 * @author zhangpu
 */
public class PageObjectWrapperFactory extends DefaultObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object object) {
        if (PageInfo.class.isAssignableFrom(object.getClass())) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        if (PageInfo.class.isAssignableFrom(object.getClass())) {
            return new PageObjectWrapper((PageInfo) object);
        }
        throw new ReflectionException(
                "The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}
