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
    }

    @Test
    public void isChineseIp() {
        //183.230.21.18
        log.info("Chinese IP test {}:{}", "125.85.30.118", isChineseIp("125.85.30.118"));
    }

    public static boolean isChineseIp(String ip) {
        String chineseIps = HttpRequest.get("http://www.ipdeny.com/ipblocks/data/aggregated/cn-aggregated.zone").body();
        try (BufferedReader br = new BufferedReader(new StringReader(chineseIps))) {
            if (IPUtil.isInRange(ip, Strings.trimToEmpty(br.readLine()))) {
                return true;
            }
        } catch (Exception e) {
            // ig
        }
        return false;
    }
}
