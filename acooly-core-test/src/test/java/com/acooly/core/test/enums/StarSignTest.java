package com.acooly.core.test.enums;

import com.acooly.core.common.enums.StarSign;
import com.acooly.core.utils.Dates;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangpu
 * @date 2019-05-23 15:27
 */
@Slf4j
public class StarSignTest {

    @Test
    public void testStarSign() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        log.info("{}-{} : {}", month, day, StarSign.to(month, day));

        //边界测试
        log.info("12-28 : {}", StarSign.to(12, 28));
        log.info("1-3 : {}", StarSign.to(1, 3));
    }


    @Test
    public void testBoundary() {
        log.info("9-23 : {}", StarSign.to(9, 23));
        log.info("9-24 : {}", StarSign.to(9, 24));

    }


}
