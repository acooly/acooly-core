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

/**
 * @author zhangpu
 * @date 2018-09-20 22:33
 */
@Slf4j
public class ValidatorsTest {


    public static void main(String[] args) {
        CustomJsr303PropertyBean bean = new CustomJsr303PropertyBean();
        bean.setCertNo("51022119820915641x");
        bean.setMobileNo("19909091989");
        bean.setPrice(Money.cent(100));
        bean.setUrl("http://acooly.cn");
        Validators.assertJSR303(bean);
    }


    @Getter
    @Setter
    static class CustomJsr303PropertyBean {

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
