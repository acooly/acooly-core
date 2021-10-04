/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-10-28 00:33
 */
package com.acooly.core.test.utils;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.system.IPUtil;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Locale;

/**
 * @author zhangpu
 * @date 2020-10-28 00:33
 */
@Slf4j
public class IPUtilTest {

    @Test
    public void isRange() {
        // 增加IPUtil工具方法isInRange,判断IP是否在指定网段内（子网）。例如：`IPUtil.isInRange("192.168.1.2", "192.168.0.0/24")`
        log.info("in RANGE {}", IPUtil.isInRange("192.168.1.127", "192.168.1.64/26"));
        System.out.println(IPUtil.isInRange("125.85.30.118", "125.85.0.0/19"));

        log.info("{}", Locale.CHINA.getCountry());

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            log.info("中文名称：{} , 英文名称：{}, 国家简称：{}, 语言简称：{}"
                    ,locale.getDisplayCountry()
                    ,locale.getDisplayCountry(Locale.ENGLISH)
                    ,locale.getCountry()
                    ,locale.toString());

        }

    }

    @Test
    public void isChineseIp() {
        //183.230.21.18
        log.info("Chinese IP test {}:{}", "125.85.30.118", isChineseIp("125.85.30.118"));
    }

    public static boolean isChineseIp(String ip) {
        String chineseIps = HttpRequest.get("http://www.ipdeny.com/ipblocks/data/countries/cn.zone").body();
        try (BufferedReader br = new BufferedReader(new StringReader(chineseIps))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (IPUtil.isInRange(ip, Strings.trimToEmpty(line))) {
                    log.info("ip range match {} -> {}", line, ip);
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
