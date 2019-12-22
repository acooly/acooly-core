package com.acooly.core.common.service;

import com.acooly.core.common.dao.EntityDao;
import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.domain.Sortable;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Exceptions;
import com.acooly.core.utils.Reflections;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public abstract class EntityServiceImpl<T, M extends EntityDao<T>>
        implements ApplicationContextAware, EntityService<T> {

    private M entityDao;
    private ApplicationContext context;

    protected M getEntityDao() throws BusinessException {
        if (entityDao != null) {
            return entityDao;
        }
        // 获取定义的第一个实例变量类型
        Class<M> daoType = Reflections.getSuperClassGenricType(getClass(), 1);
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
        if (Collections3.isNotEmpty(ts)) {
            getEntityDao().saves(ts);
        }
    }

    @Override
    public void inserts(List<T> ts) throws BusinessException {
        if (Collections3.isNotEmpty(ts)) {
            getEntityDao().inserts(ts);
        }
    }

    @Override
    public void update(T o) throws BusinessException {
        getEntityDao().update(o);
    }

    @Override
    public void saveOrUpdate(T t) throws BusinessException {
        Assert.notNull(t, "实体不能为空");
        if (t instanceof Entityable) {
            if (((Entityable) t).getId() != null) {
                update(t);
            } else {
                save(t);
            }
        } else {
            throw new UnsupportedOperationException("实体类必须继承Entityable才能使用saveOrUpdate方法");
        }
    }

    /**
     * 置顶操
     * 设置sortTime为当前时间
     *
     * @param id
     */
    @Override
    public void top(Serializable id) {
        Class<T> entityType = Reflections.getSuperClassGenricType(getClass());
        if (!Sortable.class.isAssignableFrom(entityType)) {
            Exceptions.rethrow(CommonErrorCodes.UNSUPPORTED_ERROR);
        }
        T t = get(id);
        Sortable sortable = (Sortable) t;
        sortable.setSortTime(System.currentTimeMillis());
        update(t);
    }

    @Override
    public void up(Serializable id) {
        up(id, null, null);
    }

    @Override
    public void up(Serializable id, Map<String, Object> map, Map<String, Boolean> sortMap) {
        Class<T> entityType = Reflections.getSuperClassGenricType(getClass());
        if (!Sortable.class.isAssignableFrom(entityType)) {
            Exceptions.rethrow(CommonErrorCodes.UNSUPPORTED_ERROR);
        }
        Sortable current = (Sortable) get(id);
        Long currentSortTime = current.getSortTime();
        if (map == null) {
            map = Maps.newHashMap();
        }
        if (map.size() == 0) {
            map.put("GT_sortTime", currentSortTime);
        }
        if (sortMap == null) {
            sortMap = Maps.newLinkedHashMap();
        }
        if (sortMap.size() == 0) {
            sortMap.put("sortTime", true);
        }
        PageInfo<T> pageInfo = new PageInfo<>(1, 1);
        query(pageInfo, map, sortMap);

        if (Collections3.isNotEmpty(pageInfo.getPageResults())) {
            Sortable up = (Sortable) Collections3.getFirst(pageInfo.getPageResults());
            current.setSortTime(up.getSortTime());
            up.setSortTime(currentSortTime);
            update((T) up);
        } else {
            current.setSortTime(System.currentTimeMillis());
        }
        update((T) current);
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
