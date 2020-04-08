/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.predicate;

import java.net.URL;

/**
 * Http Url格式断言
 *
 * @author zhangpu
 */
public final class HttpUrlPredicate implements Predicate<String> {

    public static final HttpUrlPredicate INSTANCE = new HttpUrlPredicate();

    private HttpUrlPredicate() {
    }

    @Override
    public boolean apply(String input) {
        return input != null && !"".equals(input) && isUrl(input);
    }

    protected boolean isUrl(String input) {
        if (!input.toLowerCase().startsWith("http") && !input.toLowerCase().startsWith("https")) {
            return false;
        }
        try {
            URL url = new URL(input);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
