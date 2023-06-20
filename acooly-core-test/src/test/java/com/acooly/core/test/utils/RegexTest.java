/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-01 17:35
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.Regexs;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @author zhangpu
 * @date 2019-09-01 17:35
 */
@Slf4j
public class RegexTest {

    @Test
    public void testFinder() {
        String hanzi = "我是'中国人'！1111i \"lo\"ve china";
        System.out.println(hanzi + " [finder first] : " + Regexs.finder(Regexs.CommonRegex.QUITATION, hanzi));
        System.out.println(hanzi + " [finders all] : " + Regexs.finders(Regexs.CommonRegex.QUITATION, hanzi));

    }

    @Test
    public void testRegexFinder() {
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})";
        String text = "today is 2019-09-01, yesterday is 2019-08-31";
        Pattern p = Pattern.compile(regex);
        System.out.printf("finders: %s\n", Regexs.finders(p, text));
        System.out.printf("finderWholes: %s\n", Regexs.finderWholes(p, text));
        System.out.printf("finder: %s\n", Regexs.finder(p, text));
    }


}
