package com.acooly.core.common.domain;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.mapper.BeanCopier;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 统一定义id的entity基类
 *
 * <p>实体基类
 *
 * @author zhangpu
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity implements Entityable {
  /** UID */
  private static final long serialVersionUID = -1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 创建时间 */
  private Date createTime = new Date();

  /** 修改时间 */
  private Date updateTime = new Date();

  public static <T, S extends AbstractEntity> List<T> to(List<S> list, Class<T> clazz) {
    if (list == null || list.isEmpty()) {
      return Lists.newArrayList();
    }
    List<T> ts = new ArrayList<>(list.size());
    for (S s : list) {
      ts.add(s.to(clazz));
    }
    return ts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AbstractEntity)) return false;

    AbstractEntity that = (AbstractEntity) o;

    return id != null ? id.equals(that.id) : that.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  public <T> T to(Class<T> clazz) {
    try {
      T t = clazz.newInstance();
      BeanCopier.copy(
          this, t, BeanCopier.CopyStrategy.IGNORE_NULL, BeanCopier.NoMatchingRule.IGNORE);
      return t;
    } catch (Exception e) {
      throw new BusinessException(e);
    }
  }

  @Override
  public String toString() {
    return ToString.toString(this);
  }
}
