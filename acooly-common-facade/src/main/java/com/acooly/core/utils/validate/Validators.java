package com.acooly.core.utils.validate;

import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.Strings;
import org.apache.commons.lang3.Validate;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Set;

/**
 * 参数验证工具
 *
 * <p>扩展Apache validate，增加jsr303或自定义验证支持
 *
 * @author zhangpu
 * @date 2014年5月16日
 */
public class Validators extends Validate {

    /**
     * 检查参数是否为空
     *
     * @param paraName
     * @param reqValue
     */
    public static void assertEmpty(String paraName, String reqValue) {
        if (Strings.isBlank(reqValue)) {
            throw new IllegalArgumentException("参数[" + paraName + "]是必须的");
        }
    }

    public static Object assertJSR303(Object object, Class<?>... groups) {
        return assertJSR303(object, null, groups);
    }

    public static Object assertJSR303(
            Object object, ValidatorFactory validatorFactory, Class<?>... groups) {
        if (validatorFactory == null) {
            validatorFactory = HibernateValidatorFactory.getInstance();
        }
        Objects.requireNonNull(object);
        Set<ConstraintViolation<Object>> constraintViolations =
                validatorFactory.getValidator().validate(object, groups);
        validateJsr303(object, constraintViolations);
        return object;
    }

    private static <T> void validateJsr303(
            Object object, Set<ConstraintViolation<T>> constraintViolations) {
        OrderCheckException exception = null;
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            exception = new OrderCheckException();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                exception.addError(
                        constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        if (exception != null) {
            throw exception;
        }
    }
}
