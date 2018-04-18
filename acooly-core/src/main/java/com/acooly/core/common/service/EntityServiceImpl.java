package com.acooly.core.common.service;

import com.acooly.core.common.dao.EntityDao;
import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.GenericsUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 基础EntityService的抽象实现。
 *
 * @param <T> 被管理的实体类
 * @param <M> 实体类的DAO
 * @author zhangpu
 */
@Transactional
public abstract class EntityServiceImpl<T, M extends EntityDao<T>>
        implements ApplicationContextAware, EntityService<T> {

    private M entityDao;
    private ApplicationContext context;

    @SuppressWarnings("unchecked")
    protected M getEntityDao() throws BusinessException {
        if (entityDao != null) {
            return entityDao;
        }
        // 获取定义的第一个实例变量类型
        Class<M> daoType = GenericsUtils.getSuperClassGenricType(getClass(), 1);
        List<Field> fields = BeanUtils.getFieldsByType(this, daoType);
        try {
            if (fields != null && fields.size() > 0) {
                entityDao = (M) BeanUtils.getDeclaredProperty(this, fields.get(0).getName());
            } else {
                entityDao = (M) context.getBean(daoType);
            }
        } catch (IllegalAccessException e) {
            throw new BusinessException(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            throw new BusinessException(e.getMessage(), e);
        }
        return entityDao;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public T get(Serializable id) throws BusinessException {
        return getEntityDao().get(id);
    }

    @Override
    public List<T> getAll() throws BusinessException {
        return getEntityDao().getAll();
    }

    @Override
    public void remove(T o) throws BusinessException {
        getEntityDao().remove(o);
    }

    @Override
    public void removeById(Serializable id) throws BusinessException {
        getEntityDao().removeById(id);
    }

    @Override
    public void removes(Serializable... ids) throws BusinessException {
        getEntityDao().removes(ids);
    }

    @Override
    public void save(T o) throws BusinessException {
        getEntityDao().create(o);
    }

    @Override
    public void saves(List<T> ts) throws BusinessException {
        getEntityDao().saves(ts);
    }

    @Override
    public void update(T o) throws BusinessException {
        getEntityDao().update(o);
    }

    @Override
    public void saveOrUpdate(T t) throws BusinessException {
        Assert.notNull(t);
        if (t instanceof Entityable) {
            if (((Entityable) t).getId() != null) {
                getEntityDao().update(t);
            } else {
                getEntityDao().create(t);
            }
        } else {
            throw new UnsupportedOperationException("实体类必须继承Entityable才能使用saveOrUpdate方法");
        }
    }

    @Override
    public PageInfo<T> query(
            PageInfo<T> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap)
            throws BusinessException {
        return getEntityDao().query(pageInfo, map, orderMap);
    }

    @Override
    public PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map) throws BusinessException {
        return query(pageInfo, map, null);
    }

    @Override
    public List<T> query(Map<String, Object> map, Map<String, Boolean> sortMap) {
        return getEntityDao().list(map, sortMap);
    }
}
