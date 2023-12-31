package com.acooly.core.common.service;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 针对单个Entity对象的操作定义. 不依赖于具体ORM实现方案.
 *
 * @author zhangpu
 */
public interface EntityService<T> {

    /**
     * 根据ID获得单个对象
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    T get(Serializable id) throws BusinessException;

    /**
     * 获得对象所有集合
     *
     * @return
     * @throws BusinessException
     */
    List<T> getAll() throws BusinessException;

    /**
     * 保存对象
     *
     * @param t
     * @throws BusinessException
     */
    void save(T t) throws BusinessException;

    /**
     * 主键不为空时更新，为空时新增
     *
     * @param t
     * @throws BusinessException
     */
    void saveOrUpdate(T t) throws BusinessException;

    /**
     * 批量保存对象，id为空时新增，id不为空时修改
     *
     * @param ts
     * @throws BusinessException
     */
    void saves(List<T> ts) throws BusinessException;

    /**
     * 批量新增对象(仅做插入不做更新，id不能为空)
     *
     * @param ts
     * @throws BusinessException
     */
    void inserts(List<T> ts) throws BusinessException;

    /**
     * 更新对象
     *
     * @param t
     * @throws BusinessException
     */
    void update(T t) throws BusinessException;

    /**
     * 删除对象
     *
     * @param t
     * @throws Exception
     */
    void remove(T t) throws BusinessException;

    /**
     * 根据ID删除对象
     *
     * @param id
     * @throws Exception
     */
    void removeById(Serializable id) throws BusinessException;

    void removes(Serializable... ids) throws BusinessException;

    /**
     * 置顶
     * T 必须实现Sortable
     *
     * @param id
     */
    void top(Serializable id);

    /**
     * 上移（默认sortTime条件和倒序查询）
     *
     * @param id
     */
    void up(Serializable id);

    /**
     * 上移（自定义前一个的查询条件）
     *
     * @param id
     * @param map
     * @param sortMap
     */
    void up(Serializable id, Map<String, Object> map, Map<String, Boolean> sortMap);

    /**
     * 带条件和排序的分页查询
     *
     * @param pageInfo
     * @param map      propertyName([EQ/LIKE...]_propertyName) --> value
     * @param sortMap  propertyName --> true:asc,false:desc
     * @return
     * @throws BusinessException
     */
    PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map, Map<String, Boolean> sortMap)
            throws BusinessException;

    /**
     * 多条件分页查询
     *
     * @param pageInfo
     * @param map
     * @return
     * @throws BusinessException
     */
    PageInfo<T> query(PageInfo<T> pageInfo, Map<String, Object> map) throws BusinessException;

    /**
     * 多条件多字段排序列表查询
     *
     * @param map
     * @param sortMap
     * @return
     */
    List<T> query(Map<String, Object> map, Map<String, Boolean> sortMap);

    /**
     * 树形结构数据查询
     * T必须实现TreeNode接口
     *
     * @param map
     * @param sortMap
     * @return
     */
//    List<T> tree(Map<String, Object> map, Map<String, Boolean> sortMap);


}
