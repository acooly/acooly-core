package com.acooly.core.test.enums;

import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.utils.Dates;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

/**
 * @author zhangpu
 * @date 2019-05-23 15:27
 */
@Slf4j
public class AnimalSignTest {

    @Test
    public void testAnimalSign() {
        Date date = new Date();
        log.info("日期 {} : {}", Dates.format(date, Dates.CHINESE_DATE_FORMAT_LINE), AnimalSign.to(date));
        log.info("年份 {} : {}", "1982", AnimalSign.to(1982));

    }
}
