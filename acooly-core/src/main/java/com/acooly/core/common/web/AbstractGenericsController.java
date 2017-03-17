package com.acooly.core.common.web;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.UnMappingMethodException;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.GenericsUtils;
import com.acooly.core.utils.system.IPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 抽象泛型控制器
 * <p>
 * 提供根据泛型获取子类的受管实体和实体服务的信息，包括名称，类名和实例等。
 *
 * @param <T>
 * @param <M>
 * @author zhangpu
 */
public abstract class AbstractGenericsController<T extends Entityable, M extends EntityService<T>> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGenericsController.class);

    /**
     * 所管理的Entity类型.
     */
    protected Class<T> entityClass;
    /**
     * 管理Entity所用的 Service
     */
    private M entityService;

    protected String allowMapping = "*";
    @Autowired(required = false)
    private Validator[] validators;


    protected void bind(HttpServletRequest request, Object command) throws Exception {
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.bind(request);
        if (this.validators != null) {
            for (Validator validator : this.validators) {
                if (validator.supports(command.getClass())) {
                    ValidationUtils.invokeValidator(validator, command, binder.getBindingResult());
                }
            }
        }
        binder.closeNoCatch();
    }

    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, "command");
        initBinder(request, binder);
        return binder;
    }

    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
    }

    protected void allow(HttpServletRequest request, HttpServletResponse response, MappingMethod mappingMethod) {
        if (!MappingMethod.allow(allowMapping, mappingMethod)) {
            logger.warn("不支持的自动Mapping方法：{}, IP:{}", mappingMethod, IPUtil.getIpAddr(request));
            throw new UnMappingMethodException();
        }
    }

    /**
     * 基于@ExceptionHandler异常处理
     */
    @ExceptionHandler(UnMappingMethodException.class)
    public String allowExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        try {
            response.sendError(404);
        } catch (Exception e) {
            logger.error("response.sendError",e);
        }
        return null;
    }

    /**
     * 取得entityClass的函数
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
        return entityClass;
    }

    /**
     * 获取所管理的对象名.首字母小写，如"user"
     */
    protected String getEntityName() {
        return StringUtils.uncapitalize(ClassUtils.getShortName(getEntityClass()));
    }

    /**
     * 获取所管理的对象名.首字母小写，如"user"
     */
    protected String getEntityListName() {
        return getEntityName() + "s";
    }

    /**
     * 获得EntityManager类进行CRUD操作，可以在子类重载.
     */
    @SuppressWarnings("unchecked")
    protected M getEntityService() {
        if (entityService == null) {
            Class<M> entityServiceClass = GenericsUtils.getSuperClassGenricType(getClass(), 1);
            List<Field> fields = BeanUtils.getFieldsByType(this, entityServiceClass);
            try {
                if (fields.isEmpty()) {
                    entityService = ApplicationContextHolder.get().getBean(entityServiceClass);
                } else {
                    entityService = (M) BeanUtils.getDeclaredProperty(this, fields.get(0).getName());
                }
            } catch (IllegalAccessException e) {
                logger.error("获得EntityManager类失败",e);
            } catch (NoSuchFieldException e) {
                logger.error("获得EntityManager类失败",e);
            }
            Assert.notNull(entityService, "EntityService未能成功初始化");
        }
        return entityService;
    }

}
