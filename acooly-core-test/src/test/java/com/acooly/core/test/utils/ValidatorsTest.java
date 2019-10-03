package com.acooly.core.test.utils;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.validate.Validators;
import com.acooly.core.utils.validate.jsr303.CertNo;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.core.utils.validate.jsr303.MobileNo;
import com.acooly.core.utils.validate.jsr303.MoneyConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * @author zhangpu
 * @date 2018-09-20 22:33
 */
@Slf4j
public class ValidatorsTest {

    @Test
    public void testBeanValidatorWithJsr303() {
        CustomJsr303PropertyBean bean = new CustomJsr303PropertyBean();
        bean.setCertNo("51022119820915641x");
        bean.setMobileNo("19909091989");
        bean.setPrice(Money.cent(100));
        bean.setUrl("http://acooly.cn");
        bean.setTitle("title");
//        bean.setName("name");

        Validators.assertJSR303(bean);
        log.info("validator ok with custom JSR303");
    }


    @Getter
    @Setter
    static class CustomJsr303PropertyBean {

        /**
         * hibernate的NotEmpty
         */
        @org.hibernate.validator.constraints.NotEmpty
        private String title;

        /**
         * javax的NotBlank
         */
        @NotEmpty
        private String name;

        @CertNo
        private String certNo;

        @MobileNo
        private String mobileNo;

        @MoneyConstraint(min = 100)
        private Money price;

        @HttpUrl
        private String url;

    }


}
