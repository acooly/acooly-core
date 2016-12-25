/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-03-22 11:00 创建
 */
package com.acooly.module.security.defence.csrf;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qiubo
 */
public class CookieCsrfTokenRepository implements CsrfTokenRepository {
	
	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
		Cookie tokenCookie = new Cookie(token.getParameterName(), token.getToken());
		tokenCookie.setPath(request.getContextPath() + "/");
		response.addCookie(tokenCookie);
	}
	
	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		String csrf = getCsrfFromCookie(request);
		if (csrf == null) {
			return null;
		}
		return generateToken(csrf);
	}
	
	private String getCsrfFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(CsrfTokenRepository.CSRF_PARAMETER_NAME)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
