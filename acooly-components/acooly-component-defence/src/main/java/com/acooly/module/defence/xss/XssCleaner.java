/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2023-11-26 20:17
 */
package com.acooly.module.defence.xss;

import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpu
 * @date 2023-11-26 20:17
 */
@Slf4j
public class XssCleaner {

    public static String clean(String value) {
        // You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("(?i)eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        // 过滤on相关的js事件(3种模式): onXXX="", onXXX='', onXXX=
        value = value.replaceAll("(on\\w+=\"[^\"]+\")|(on\\w+='[^']+')|(on\\w+=)", "");
        value = value.replaceAll("'", "&#39;");
        // value = value.replaceAll(";", "&#59;");
        value = value.replaceAll("(?i) or ", "&#111;&#114;");
        value = value.replaceAll("(?i)%20or%20", "&#111;&#114;");
        value = value.replaceAll("(?i)script", "");
        value = Strings.trimToEmpty(value);
        return value;
    }


    public static void main(String[] args) {
        String value = "00onclick=\"alert('1')\"11onclick='alert(\"2\")'22onload=alert('1')33";
        value = XssCleaner.clean(value);
        System.out.println(value);
    }
}
