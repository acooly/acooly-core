/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-01 17:35
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.Regexs;
import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

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

}
