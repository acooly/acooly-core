package com.acooly.core.common.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.common.web.support.MultiFormatCustomDateEditor;
import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象业务操作类
 *
 * <p>负责：框架基本功能(CRUD)的服务层调用
 *
 * @param <T>
 * @param <M>
 * @author zhangpu
 */
public abstract class AbstractOperationController<T extends Entityable, M extends EntityService<T>>
        extends AbstractGenericsController<T, M> {
    /**
     * 实体ID名称
     */
    protected String entityIdName = "id";
    /**
     * 默认分页大小
     */
    protected int defaultPageSize = 15;


    /**
     * 置顶
     */
    protected void doTop(HttpServletRequest request, HttpServletResponse response, Model model) {
        Long id = Servlets.getLongParameter(getEntityIdName());
        getEntityService().top(id);
    }

    /**
     * 上移
     */
    protected void doUp(HttpServletRequest request, HttpServletResponse response, Model model) {
        Long id = Servlets.getLongParameter(getEntityIdName());
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Boolean> sortMap = Maps.newLinkedHashMap();
        customUpQuery(request, id, map, sortMap);
        getEntityService().up(id, map, sortMap);
    }

    /**
     * 预留上移查询的条件自定义方法
     * do：查询比当前id排序值大的条件
     */
    protected void customUpQuery(HttpServletRequest request, Long id, Map<String, Object> map, Map<String, Boolean> sortMap) {

    }

    /**
     * 執行分頁查詢
     *
     * @param model
     * @param request
     * @param response
     */
    protected PageInfo<T> doList(
            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        return getEntityService()
                .query(getPageInfo(request), getSearchParams(request), getSortMap(request));
    }

    protected PageInfo<T> doList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return doList(request, response, null);
    }

    /**
     * 执行查询
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    protected List<T> doQuery(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        return getEntityService().query(getSearchParams(request), getSortMap(request));
    }

    protected List<T> doQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return doQuery(request, response, null);
    }

    /**
     * 执行保存实体(根據請求中是否存在ID選擇保存或更新)
     *
     * @param request
     * @param response
     * @param model
     * @param isCreate
     */
    protected T doSave(HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate) throws Exception {
        T entity = loadEntity(request);
        if (entity == null) {
            // create
            allow(request, response, MappingMethod.create);
            entity = getEntityClass().newInstance();
        } else {
            // update
            allow(request, response, MappingMethod.update);
        }
        doDataBinding(request, entity);
        onSave(request, response, model, entity, isCreate);
        // 这里服务层默认是根据entity的Id是否为空自动判断是SAVE还是UPDATE.
        if (isCreate) {
            getEntityService().save(entity);
        } else {
            getEntityService().update(entity);
        }
        return entity;
    }

    /**
     * 根据传入的ids删除实体。
     *
     *
     * <li>ids.length ==1 则调用删除单个实体的服务 getEntityService().removeById(id)
     * <li>ids.length > 1 则调用批量删除服务 getEntityService().removes(ids);
     *
     * @param request
     * @param response
     * @param model
     * @param ids
     */
    protected void doRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {

        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("请求参数中没有指定需要删除的实体Id");
        }
        if (ids.length == 1) {
            getEntityService().removeById(ids[0]);
        } else {
            getEntityService().removes(ids);
        }
    }

    /**
     * 从request中获取需要删除的实体的ID
     *
     * <p>依次支持：
     * <li>request.getParameterValues方式获取多个同名ID表单的值
     * <li>request.getParameter方法获取单个ID表单的值，如果该值有逗号分割，则分割为多个ID
     *
     * @param request
     * @param response
     * @param model
     */
    @Deprecated
    protected void doRemove(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String[] ids = request.getParameterValues(getEntityIdName());
        List<Long> idList = Lists.newArrayList();
        getIds(idList, ids);
        if (idList.isEmpty()) {
            throw new IllegalArgumentException("请求参数中没有指定需要删除的实体Id");
        }
        Serializable[] lid = idList.toArray(new Long[]{});
        doRemove(request, response, model, lid);
    }

    /**
     * 从request中获取需要删除的实体的ID
     *
     * <p>依次支持：
     * <li>request.getParameterValues方式获取多个同名ID表单的值
     * <li>request.getParameter方法获取单个ID表单的值，如果该值有逗号分割，则分割为多个ID
     *
     * @param request
     * @return
     */
    protected Serializable[] getRequestIds(HttpServletRequest request) {
        String[] ids = request.getParameterValues(getEntityIdName());
        List<Long> idList = Lists.newArrayList();
        getIds(idList, ids);
        if (idList.size() == 0) {
            throw new RuntimeException("请求参数中没有指定需要删除的实体Id");
        }
        return idList.toArray(new Long[]{});
    }

    private void getIds(List<Long> idList, String[] ids) {
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                if (StringUtils.isNotBlank(id)) {
                    if (StringUtils.contains(id, ",")) {
                        String[] subIds = StringUtils.split(id, ",");
                        for (String subId : subIds) {
                            idList.add(Long.valueOf(subId));
                        }
                    } else {
                        idList.add(Long.valueOf(id));
                    }
                }
            }
        }
    }

    /**
     * 加载实体
     *
     * @param request
     * @return
     */
    protected T loadEntity(HttpServletRequest request) {
        String id = request.getParameter(getEntityIdName());
        if (StringUtils.isNotBlank(id)) {
            return getEntityService().get(Long.valueOf(id));
        }
        return null;
    }

    /**
     * 保存实体前，自定义组装对象。用于子类扩展实体组装或保存前检查
     *
     * @param request
     * @param response
     * @param model
     * @param isCreate
     */
    protected T onSave(HttpServletRequest request, HttpServletResponse response, Model model, T entity, boolean isCreate) throws Exception {
        return entity;
    }

    /**
     * 默认分页对象
     *
     * @param request
     * @return
     */
    protected PageInfo<T> getPageInfo(HttpServletRequest request) {
        PageInfo<T> pageinfo = new PageInfo<T>();
        pageinfo.setCountOfCurrentPage(getDefaultPageSize());
        return pageinfo;
    }

    /**
     * 默认查询条件
     *
     * @param request
     * @return
     */
    protected Map<String, Object> getSearchParams(HttpServletRequest request) {
        return Servlets.getParametersStartingWith(request, "search_");
    }

    /**
     * 默认排序
     *
     * @param request
     * @return
     */
    protected Map<String, Boolean> getSortMap(HttpServletRequest request) {
        return Maps.newHashMap();
    }

    protected void doDataBinding(HttpServletRequest request, Object command) {
//        bind(request, command);
        bindNotValidator(request, command);
    }

    protected Map<String, Object> referenceData(HttpServletRequest request) {
        Map<String, Object> map = Maps.newHashMap();
        referenceData(request, map);
        return map;
    }

    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    }

    /**
     * 批量保存获取merge的列表
     *
     * @param request
     * @param exists
     * @return
     */
    protected List<T> mergeBatchEntities(HttpServletRequest request, List<T> exists) {
        return mergeBatchEntities(request, exists, getEntityClass());
    }

    protected <X extends Entityable> List<X> mergeBatchEntities(HttpServletRequest request, List<X> exists, Class<X> clazz) {
        // 获取前缀为实体名字开头的请求参数。格式：${entityName}_${propertyName}_${index}
        Map<String, String> params = Servlets.getParameters(request, Strings.uncapitalize(clazz.getSimpleName()) + "_", false);
        // 清洗数据：index -> entityDataMap
        Map<Integer, Map<String, String>> entityMap = Maps.newHashMap();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (Strings.contains(entry.getKey(), "_")) {
                Pair<String, Integer> pair = mergeBatchPropertyName(entry.getKey());
                Integer index = pair.getRight();
                if (index == null) {
                    continue;
                }
                if (entityMap.get(index) == null) {
                    entityMap.put(index, new HashMap<String, String>());
                }
                entityMap.get(index).put(pair.getLeft(), entry.getValue());
            }
        }
        List<X> entities = Lists.newArrayList();
        Map<Long, X> existingMap = Maps.newHashMap();
        for (X exist : exists) {
            existingMap.put((Long) exist.getId(), exist);
        }
        for (Map<String, String> entityDataMap : entityMap.values()) {
            String reqIdStr = entityDataMap.get("id");
            // 新增：新行
            if (Strings.isBlank(reqIdStr)) {
                entities.add(bindMap(entityDataMap, Reflections.createObject(clazz)));
                continue;
            }
            // 修改：已存在的
            Long requestId = Long.valueOf(reqIdStr);
            if (requestId != null && existingMap.get(requestId) != null) {
                entities.add(bindMap(entityDataMap, existingMap.get(requestId)));
            }
        }
        return entities;
    }


    protected T newEntity() {
        try {
            return getEntityClass().newInstance();
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCodes.INTERNAL_ERROR, "创建实体失败");
        }

    }


    /**
     * 解析批量编辑请求参数名称
     * 格式：propertyName_${index} -> Pair<propertyName,Index>
     *
     * @param key
     * @return
     */
    protected Pair<String, Integer> mergeBatchPropertyName(String key) {
        Integer index = null;
        String propertyName = null;
        if (Strings.contains(key, "_")) {
            String[] keyPairs = Strings.split(key, "_");
            propertyName = keyPairs[0];
            if (Strings.isNumber(keyPairs[1])) {
                index = Integer.valueOf(keyPairs[1]);
            }
        }
        return Pair.of(propertyName, index);
    }


    /**
     * 兼容设置
     */
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        initBinder(binder);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MultiFormatCustomDateEditor());
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public String getEntityIdName() {
        return entityIdName;
    }

    public void setEntityIdName(String entityIdName) {
        this.entityIdName = entityIdName;
    }
}
