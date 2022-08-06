/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 10:27
 */
package com.acooly.core.test.utils.ie;

import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.common.enums.ChannelEnum;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.test.core.entity.CoderCustomer;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.core.utils.ie.ExportResult;
import com.acooly.core.utils.ie.Exports;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

/**
 * @author zhangpu
 * @date 2022-08-03 10:27
 */
@Slf4j
public class ExportsTest {


    @Test
    public void testExportParse() {
        CoderCustomer customer = getCustomer();
        log.info("{}", customer);
        ExportResult exportResult = Exports.parse(customer);
        log.info("{}", exportResult);
    }

    private CoderCustomer getCustomer() {
        CoderCustomer customer = new CoderCustomer();
        customer.setUsername("zhangpu");
        customer.setAge(40);
        customer.setBirthday(Dates.parse("1982-09-15"));
        customer.setGender(Gender.male);
        customer.setAnimal(AnimalSign.Dog);
        customer.setMobileNo("13888888887");
        customer.setMail("zhangpu@acooly.cn");
        customer.setContent("这你是内容字段，提供内容文字说明。这你是内容字段，提供内容文字说明。这你是内容字段，提供内容文字说明。");
        customer.setDoneRatio(50);
        customer.setPayRate(Money.amout("98.77"));
        customer.setSalary(Money.amout("50000"));
        customer.setRegistryChannel(ChannelEnum.ANDROID);
        customer.setPushAdv(WhetherStatus.no);
        customer.setNumStatus(2);
        customer.setWebsite("https://acooly.cn");
        customer.setPhotoPath("https://acooly.cn/assets/image/icon/logo-acooly-white.png");
        customer.setStatus(SimpleStatus.enable);
        customer.setComments("这是备注星星");
        customer.setId(1L);
        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        return customer;
    }
}
