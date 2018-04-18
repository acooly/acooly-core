/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-06-25 11:22 创建
 *
 */
package com.acooly.module.defence.csrf;

import javax.servlet.http.HttpServletRequest;

/**
 * Simple strategy to match an <tt>HttpServletRequest</tt>.
 *
 * @author Luke Taylor
 * @since 3.0.2
 */
public interface RequestMatcher {

    /**
     * Decides whether the rule implemented by the strategy matches the supplied request.
     *
     * @param request the request to check for a match
     * @return true if the request matches, false otherwise
     */
    boolean matches(HttpServletRequest request);
}
