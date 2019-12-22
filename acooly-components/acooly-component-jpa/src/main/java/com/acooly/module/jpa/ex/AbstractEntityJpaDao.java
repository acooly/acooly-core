package com.acooly.module.jpa.ex;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.dao.support.SearchFilter;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.utils.BeanUtils;
import com.acooly.module.jpa.EntityJpaDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * EntityDao接口 JAP封装实现
 *
 * @param <T>
 * @param <ID>
 * @author zhangpu
 * @date 2012年8月30日
 */
public class AbstractEntityJpaDao<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements EntityJpaDao<T, ID> {

    private static final String CREATE_TIME_P_NAME = "createTime";
    private static final String UPDATE_TIME_P_NAME = "updateTime";

    private final JpaEntityInformation<T, ?> entityInformation;
    private Class<T> domainClass;
    private EntityManager em;

    public AbstractEntityJpaDao(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        // Keep the EntityManager around to used from the newly introduced methods.
        this.em = entityManager;
        this.domainClass = entityInformation.getJavaType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Serializable id) throws DataAccessException {
        return findOne(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id));
            }
        }).get();
    }

    @Transactional
    @Override
    public void create(T o) throws DataAccessException {
        save(o);
    }

    @Transactional
    @Override
    public void update(T o) throws DataAccessException {
        save(o);
    }

    @Transactional
    @Override
    public void saves(List<T> entities) {
        save(entities);
    }

    @Override
    @Transactional
    public void inserts(List<T> entities) {
        save(entities);
    }

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        if (entityInformation.isNew(entity)) {
            onCreate(entity);
            onUpdate(entity);
            em.persist(entity);
            return entity;
        } else {
            onUpdate(entity);
            return em.merge(entity);
        }
    }

    @Transactional
    public <S extends T> List<S> save(Iterable<S> entities) {
        List<S> result = new ArrayList();
        if (entities == null) {
            return result;
        } else {
            Iterator<S> var3 = entities.iterator();
            while (var3.hasNext()) {
                S entity = var3.next();
                result.add(this.save(entity));
            }
            return result;
        }
    }

    private void onCreate(T entity) {
        if (entity instanceof Entityable) {
            ((Entityable) entity).setCreateTime(new Date());
        } else {
            try {
                Field f = BeanUtils.getDeclaredField(entity, CREATE_TIME_P_NAME);
                if (f != null && f.getType().isAssignableFrom(Date.class)) {
                    BeanUtils.setDeclaredProperty(entity, f, new Date());
                }
            } catch (Exception e) {
            }
        }
    }

    private void onUpdate(T entity) {
        if (entity instanceof Entityable) {
            ((Entityable) entity).setUpdateTime(new Date());
        } else {
            try {
                Field f = BeanUtils.getDeclaredField(entity, UPDATE_TIME_P_NAME);
                if (f != null && f.getType().isAssignableFrom(Date.class)) {
                    BeanUtils.setDeclaredProperty(entity, f, new Date());
                }
            } catch (Exception e) {
            }
        }
    }

    @Transactional
    @Override
    public void remove(T o) throws DataAccessException {
        delete(o);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public void removeById(Serializable id) throws DataAccessException {
        deleteById((ID) id);
    }

    @Transactional
    @Override
    public void removes(Serializable... ids) {
        Iterator<Serializable> iterator = Lists.newArrayList(ids).iterator();
        String queryString = String.format(QueryUtils.DELETE_ALL_QUERY_STRING, getEntityName());
        String alias = QueryUtils.detectAlias(queryString);
        StringBuilder builder = new StringBuilder(queryString);
        builder.append(" where");
        int i = 0;
        while (iterator.hasNext()) {
            iterator.next();
            builder.append(String.format(" %s.id = ?%d", alias, ++i));
            if (iterator.hasNext()) {
                builder.append(" or");
            }
        }
        Query query = em.createQuery(builder.toString());
        iterator = Lists.newArrayList(ids).iterator();
        i = 0;
        while (iterator.hasNext()) {
            query.setParameter(++i, iterator.next());
        }
        query.executeUpdate();
    }

    @Override
    public List<T> getAll() throws DataAccessException {
        return findAll();
    }

    @Override
    public List<T> list(Map<String, Object> map, Map<String, Boolean> sortMap) {
        Specification<T> spec = buildSpecification(map);
        Sort sort = buildSort(sortMap);
        return findAll(spec, sort);
    }

    @Override
    public PageInfo<T> query(
            PageInfo<T> pageInfo, Map<String, Object> map, Map<String, Boolean> sortMap)
            throws DataAccessException {
        Specification<T> spec = buildSpecification(map);
        Sort sort = buildSort(sortMap);
        Pageable pageable =
                new PageRequest(pageInfo.getCurrentPage() - 1, pageInfo.getCountOfCurrentPage(), sort);
        Page<T> page = findAll(spec, pageable);
        pageInfo.setPageResults(page.getContent());
        pageInfo.setTotalCount(page.getTotalElements());
        pageInfo.setTotalPage(page.getTotalPages());
        return pageInfo;
    }

    @Override
    protected Class<T> getDomainClass() {
        return domainClass;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    protected String getEntityName() {
        return domainClass.getSimpleName();
    }

    /**
     * 生成查询条件
     *
     * @param map
     * @return
     */
    private Specification<T> buildSpecification(Map<String, Object> map) {
        List<SearchFilter> searchFilters = SearchFilter.parse(map);
        return DynamicSpecifications.bySearchFilter(searchFilters, null);
    }

    /**
     * 生成查询排序
     *
     * @param orderMap
     * @return
     */
    private Sort buildSort(Map<String, Boolean> orderMap) {
        List<Order> orders = new ArrayList<Order>();
        if (orderMap != null && orderMap.size() > 0) {
            for (Map.Entry<String, Boolean> entry : orderMap.entrySet()) {
                orders.add(new Order(entry.getValue() ? Direction.ASC : Direction.DESC, entry.getKey()));
            }
        } else {
            orders.add(new Order(Direction.DESC, "id"));
        }
        Sort sort = new Sort(orders);
        return sort;
    }

    @Override
    public List<T> find(String property, Object value) {
        Map<String, Object> searchFilters = Maps.newHashMap();
        searchFilters.put(property, value);
        Specification<T> spec = buildSpecification(searchFilters);
        return findAll(spec);
    }

    @Override
    public T findUniqu(String property, Object value) {
        Map<String, Object> searchFilters = Maps.newHashMap();
        searchFilters.put(property, value);
        Specification<T> spec = buildSpecification(searchFilters);
        return findOne(spec).get();
    }
}