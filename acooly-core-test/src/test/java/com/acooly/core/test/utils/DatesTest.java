/*
 * www.acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2018-07-25 16:50 创建
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.Dates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangpu 2018-07-25 16:50
 */
@Slf4j
public class DatesTest {

    @Test
    public void testDates() {

        log.info(Dates.format(new Date()));


    }


    @Test
    public void testSub() throws Exception {

        Date birthday = Dates.parse("1982-07-15");
        System.out.println(Dates.sub(new Date(), birthday, Calendar.YEAR));

    }

    @Test
    public void testSubDate() {
        Date now = new Date();
        log.info("now: {}", Dates.format(now));
        long size = 2;
        log.info("subDate by {} day: {}", size, Dates.format(Dates.subDate(now, size, TimeUnit.DAYS)));
        log.info("subDate by {} seconds: {}", size, Dates.format(Dates.subDate(now, size, TimeUnit.SECONDS)));
        log.info("subDate by {} hours: {}", size, Dates.format(Dates.subDate(now, size, TimeUnit.HOURS)));
    }


    @Test
    public void testGetDate() throws Exception {
        Date date = Dates.parse("1982-07-15 23:11:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date day = calendarDate.getTime();

        log.info("getDate Date {} day: {}", Dates.format(date), Dates.format(day));
    }

    @Test
    public void testIsDate() {
        Date datetime = Dates.parse("1982-07-15 00:00:00");
        log.info("Dates.isDate() Date {} isDate: {}", Dates.format(datetime), Dates.isDate(datetime));
        Date date = Dates.parse("1982-07-15");
        log.info("Dates.isDate() Date {} isDate: {}", Dates.format(date), Dates.isDate(date));
        Date datetime1 = Dates.parse("1982-07-15 12:00:00");
        log.info("Dates.isDate() Date {} isDate: {}", Dates.format(datetime1), Dates.isDate(datetime1));
    }


    @Test
    public void testCompare() {
        Date datetime1 = Dates.parse("1982-07-15 00:00:00");
        Date datetime11 = Dates.parse("1982-07-15 00:00:00");
        Date datetime2 = Dates.parse("1982-07-18 12:00:00");
        Date datetime22 = Dates.parse("1982-07-18 12:00:00");

        log.info("Dates.gt({},{}) : {}", Dates.format(datetime1), Dates.format(datetime2), Dates.gt(datetime1, datetime2));
        log.info("Dates.gte({},{}) : {}", Dates.format(datetime1), Dates.format(datetime11), Dates.gte(datetime1, datetime11));
        log.info("Dates.gte({},{}) : {}", Dates.format(datetime2), Dates.format(datetime1), Dates.gte(datetime2, datetime1));

        log.info("Dates.lt({},{}) : {}", Dates.format(datetime1), Dates.format(datetime2), Dates.lt(datetime1, datetime2));
        log.info("Dates.lte({},{}) : {}", Dates.format(datetime2), Dates.format(datetime22), Dates.lte(datetime2, datetime22));
        log.info("Dates.lte({},{}) : {}", Dates.format(datetime2), Dates.format(datetime1), Dates.lte(datetime2, datetime1));
    }

    @Test
    public void testIsOverlap() {
        Date start = Dates.parse("1982-07-15");
        Date end = Dates.parse("1984-09-15");
        Pair<Date, Date> period = Pair.of(start, end);

        Pair<Date, Date> item = Pair.of(Dates.parse("1983-07-15"), Dates.parse("1986-07-15"));
        List<Pair<Date, Date>> periods = Lists.newArrayList();
        periods.add(item);

        log.info("Dates.isOverlap: {}", Dates.isOverlap(period, item));



    }

}
