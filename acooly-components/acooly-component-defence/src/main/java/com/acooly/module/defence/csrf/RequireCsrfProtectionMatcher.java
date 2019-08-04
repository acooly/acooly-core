/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-23 21:18 创建
 */
package com.acooly.module.defence.csrf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author qiubo
 */
@Slf4j(topic = "acooly-component-defence")
public class RequireCsrfProtectionMatcher implements RequestMatcher {

    public static final String POST = "POST";
    private List<String> exclusions;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public RequireCsrfProtectionMatcher(List<String> exclusions) {
        log.info("csrf忽略uris:{}", exclusions);
        this.exclusions = exclusions;
    }

    /**
     * @return true=需要过滤 false=不需要过滤
     */
    @Override
    public boolean matches(HttpServletRequest request) {
        if (POST.equals(request.getMethod())) {
            String uri = request.getRequestURI();
            int idx = uri.indexOf(';');
            if (idx > -1) {
                uri = uri.substring(0, idx);
            }
            for (String ignoreAntPathMatcherPattern : exclusions) {
                if (antPathMatcher.match(ignoreAntPathMatcherPattern, uri)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
