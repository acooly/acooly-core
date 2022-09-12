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
import com.acooly.core.test.core.entity.App;
import com.acooly.core.test.core.entity.CoderCustomer;
import com.acooly.core.utils.BigMoney;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.core.utils.ie.ExportModelMeta;
import com.acooly.core.utils.ie.ExportOrder;
import com.acooly.core.utils.ie.Exports;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @author zhangpu
 * @date 2022-08-03 10:27
 */
@Slf4j
public class ExportsTest {

    @Test
    public void testExportExcelOneSheet() {
        String outputFilePath = "/Users/zhangpu/temp/export/customer.xlsx";
        ExportOrder order = new ExportOrder("客户信息列表", getCustomers(), outputFilePath);
        Exports.exportExcel(order);
    }

    @Test
    public void testExportExcelMultiSheet() {
        String outputFilePath = "/Users/zhangpu/temp/export/customer.xlsx";
        ExportOrder order = new ExportOrder(outputFilePath);
        order.addExportSheet(getCustomers());
        order.addExportSheet(getApps());
        Exports.exportExcel(order);
    }

    @Test
    public void testExportParseCustomer() {
        CoderCustomer customer = getCustomer(1);
        log.info("{}", customer);
        ExportModelMeta exportModelMeta = Exports.parse(customer);
        log.info("{}", exportModelMeta);
    }

    @Test
    public void testExportParseApp() {
        App app = getApp(1);
        ExportModelMeta exportModelMeta = Exports.parse(app);
        log.info("App Export Meta {}", exportModelMeta);
    }


    /**
     * 准备App列表数据
     *
     * @return
     */
    private List<App> getApps() {
        List<App> apps = Lists.newArrayList();
        for (long i = 1; i <= 10; i++) {
            apps.add(getApp(i));
        }
        return apps;
    }

    private App getApp(long i) {
        App app = new App();
        Date date = new Date();
        app.setDisplayName("应用_" + i);
        app.setName("App_" + i);
        app.setId(i);
        app.setParentAppId(0L);
        app.setParentId(0L);
        app.setCreateTime(date);
        app.setUpdateTime(date);
        app.setType("类型_" + i);
        app.setUserId(i);
        app.setPrice(Money.cent(1000 * i));
        app.setAmount(BigMoney.centOf(100000 * i));
        return app;
    }

    private List<CoderCustomer> getCustomers() {
        List<CoderCustomer> customers = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            customers.add(getCustomer(i));
        }
        return customers;
    }

    private CoderCustomer getCustomer(Integer index) {
        CoderCustomer customer = new CoderCustomer();
        customer.setUsername("zhangpu_" + index);
        customer.setAge(40 + index);
        customer.setBirthday(Dates.parse("1982-09-15"));
        customer.setGender(index % 2 == 0 ? Gender.male : Gender.female);
        customer.setAnimal(AnimalSign.Dog);
        customer.setMobileNo("1388888888" + (index % 9));
        customer.setMail("zhangpu_" + index + "@acooly.cn");
        customer.setContent("这你是内容字段，提供内容文字说明。这你是内容字段，提供内容文字说明。这你是内容字段，提供内容文字说明。");
        customer.setDoneRatio(50 + index);
        customer.setPayRate(Money.cent(7000 + index * 100));
        customer.setSalary(Money.amout("50000"));
        customer.setRegistryChannel(ChannelEnum.ANDROID);
        customer.setPushAdv(WhetherStatus.no);
        customer.setNumStatus(2);
        customer.setWebsite("https://acooly.cn");
        customer.setPhotoPath("https://acooly.cn/assets/image/icon/logo-acooly-white.png");
        customer.setStatus(SimpleStatus.enable);
        customer.setComments("这是备注星星");
        customer.setId(1L + index);
        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        return customer;
    }


}
