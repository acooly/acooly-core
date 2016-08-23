package com.acooly.core.utils;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.google.common.collect.Maps;

/**
 * 参数验证工具
 * 
 * 扩展Apache validate，增加jsr303或自定义验证支持
 * 
 * 迁移到：com.acooly.core.utils.validate.Validators
 * @see com.acooly.core.utils.validate.Validators
 * 
 * @author zhangpu
 * @date 2014年5月16日
 */
@Deprecated
public class Validates extends Validate {

	/**
	 * 检查参数是否为空
	 * 
	 * @param paraName
	 * @param reqValue
	 */
	public static void assertEmpty(String paraName, String reqValue) {
		if (StringUtils.isBlank(reqValue)) {
			throw new IllegalArgumentException("参数[" + paraName + "]是必须的");
		}
	}

	public static void assertJSR303(Object object) {
		assertJSR303(object, null);
	}

	public static void assertJSR303(Object object, ValidatorFactory validatorFactory) {
		if (validatorFactory == null) {
			validatorFactory = Validation.buildDefaultValidatorFactory();
		}
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
		Map<String, String> errors = Maps.newHashMap();
		if (constraintViolations != null && !constraintViolations.isEmpty()) {
			for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
				errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
			}
		}
		if (errors.size() > 0) {
			throw new IllegalArgumentException(errors.toString());
		}
	}

}
