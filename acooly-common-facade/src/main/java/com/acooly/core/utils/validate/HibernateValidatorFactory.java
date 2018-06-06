/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author zhangpu
 */
public class HibernateValidatorFactory {

    static ValidatorFactory validatorFactory;

    static {
        HibernateValidatorConfiguration configure =
                Validation.byProvider(HibernateValidator.class).configure();
        ResourceBundleLocator resourceBundleLocator =
                new ResourceBundleLocator() {
                    @Override
                    public ResourceBundle getResourceBundle(Locale locale) {
                        ResourceBundle resb1 = ResourceBundle.getBundle("validation/validationMessage");
                        return resb1;
                    }
                };
        configure.messageInterpolator(new ResourceBundleMessageInterpolator(resourceBundleLocator));
        validatorFactory = configure.buildValidatorFactory();
    }

    public static ValidatorFactory getInstance() {
        return validatorFactory;
    }
}
