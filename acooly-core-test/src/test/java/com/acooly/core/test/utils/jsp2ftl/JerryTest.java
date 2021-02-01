/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-12-30 16:30
 */
package com.acooly.core.test.utils.jsp2ftl;

import com.acooly.core.utils.Strings;
import jodd.jerry.Jerry;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangpu
 * @date 2020-12-30 16:30
 */
@Slf4j
public class JerryTest {

    public static void main(String[] args) {
        String jsp = "<c:forEach items=\"${allStatuss}\" var=\"e\"><option value=\"${e.key}\">${e.value}</option></c:forEach>";
        Jerry jerry = Jerry.of(jsp);
        log.info("items:{}", jerry.contents().attr("items"));
        log.info("var:{}", jerry.contents().attr("var"));

        Iterator<Jerry> options = jerry.children().iterator();
        while (options.hasNext()) {
            Jerry option = options.next();
            if (!Strings.contains(option.contents().html(), "$")) {
                continue;
            }
            log.info("option key:{}", option.contents().attr("value"));
            log.info("option value:{}", getVariable(option.get(0).getTextContent()));
        }
    }


    public static String getVariable(String script){
        String regex = "\\$\\{([\\.,[,],\\w]*)\\}";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(script);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

}
