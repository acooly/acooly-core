package com.acooly.core.common.web;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.common.exception.UnMappingMethodException;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.utils.BeanUtils;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.system.IPUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 抽象泛型控制器
 *
 * <p>提供根据泛型获取子类的受管实体和实体服务的信息，包括名称，类名和实例等。
 *
 * @param <T>
 * @param <M>
 * @author zhangpu
 */
public abstract class AbstractGenericsController<T extends Entityable, M extends EntityService<T>>
        implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGenericsController.class);

    /**
     * 所管理的Entity类型.
     */
    protected Class<T> entityClass;

    protected String allowMapping = "*";
    /**
     * 管理Entity所用的 Service
     */
    private M entityService;

    @Autowired(required = false)
    private Validator[] validators;

    protected DefaultConversionService conversionService = new DefaultConversionService();

    /**
     * 通过Map作为数据源绑定到实体
     *
     * @param map
     * @param command
     */
    protected <T> T bindMap(Map<String, String> map, T command) {
        try {
            DataBinder binder = new DataBinder(command, "command");
            binder.setConversionService(conversionService);
            MutablePropertyValues pvs = new MutablePropertyValues(map);
            binder.bind(pvs);
            binder.close();
            return command;
        } catch (Exception e) {
            throw new BusinessException("MAP_TO_ENTITY_BIND_ERROR", "绑定对象值失败", e.getMessage());
        }
    }


    protected void bind(HttpServletRequest request, Object command) {
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.setConversionService(conversionService);
        binder.bind(request);
        if (this.validators != null) {
            for (Validator validator : this.validators) {
                if (validator.supports(command.getClass())) {
                    ValidationUtils.invokeValidator(validator, command, binder.getBindingResult());
                }
            }
        }
        closeBinder(binder);
    }


    protected void bindNotValidator(HttpServletRequest request, Object command) {
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.setConversionService(conversionService);
        binder.bind(request);
        closeBinder(binder);
    }

    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, "command");
        initBinder(request, binder);
        return binder;
    }


    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
    }

    protected void closeBinder(ServletRequestDataBinder binder) {
        try {
            binder.closeNoCatch();
        } catch (Exception e) {
            List<ObjectError> objectErrors = binder.getBindingResult().getAllErrors();
            Map<String, String> errors = Maps.newLinkedHashMap();
            for (ObjectError objectError : objectErrors) {
                if (objectError.getArguments() != null) {
                    DefaultMessageSourceResolvable d = (DefaultMessageSourceResolvable) objectError.getArguments()[0];
                    errors.put(d.getDefaultMessage(), objectError.getDefaultMessage());
                }
            }
            StringBuilder sb = new StringBuilder();
            errors.forEach((k, v) -> {
                sb.append(k).append(":").append(v).append(" ,");
            });
            sb.substring(0, sb.length() - 1);
            throw new BusinessException(CommonErrorCodes.PARAMETER_ERROR, sb.toString());
        }
    }

    protected void allow(
            HttpServletRequest request, HttpServletResponse response, MappingMethod mappingMethod) {
        if (!MappingMethod.allow(allowMapping, mappingMethod)) {
            logger.warn("不支持的自动Mapping方法：{}, IP:{}", mappingMethod, IPUtil.getIpAddr(request));
            throw new UnMappingMethodException();
        }
    }

    /**
     * 基于@ExceptionHandler异常处理
     */
    @ExceptionHandler(UnMappingMethodException.class)
    public String allowExceptionHandler(
            HttpServletRequest request, HttpServletResponse response, Exception ex) {
        try {
            response.sendError(404);
        } catch (Exception e) {
            logger.error("response.sendError", e);
        }
        return null;
    }

    /**
     * 取得entityClass的函数
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        entityClass = Reflections.getSuperClassGenricType(getClass());
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
    protected M getEntityService() {
        if (entityService == null) {
            Class<M> entityServiceClass = Reflections.getSuperClassGenricType(getClass(), 1);
            List<Field> fields = BeanUtils.getFieldsByType(this, entityServiceClass);
            try {
                if (fields.isEmpty()) {
                    entityService = ApplicationContextHolder.get().getBean(entityServiceClass);
                } else {
                    entityService = (M) BeanUtils.getDeclaredProperty(this, fields.get(0).getName());
                }
            } catch (IllegalAccessException e) {
                logger.error("获得EntityManager类失败", e);
            } catch (NoSuchFieldException e) {
                logger.error("获得EntityManager类失败", e);
            }
            Assert.notNull(entityService, "EntityService未能成功初始化");
        }
        return entityService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Boolean property =
                Apps.getEnvironment()
                        .getProperty("acooly.web.enableMoneyDisplayYuan", Boolean.class, Boolean.FALSE);
        if (property) {
            conversionService.addConverter(
                    String.class,
                    Money.class,
                    source -> Strings.isNullOrEmpty(source) ? null : new Money(source));
        }
    }
}
