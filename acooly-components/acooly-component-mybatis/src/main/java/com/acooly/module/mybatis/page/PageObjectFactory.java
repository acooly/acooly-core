package com.acooly.module.mybatis.page;

import com.acooly.core.common.dao.support.PageInfo;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

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
