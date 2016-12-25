/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acooly.module.security.defence.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * An API to allow changing the method in which the expected {@link CsrfToken} is associated to the
 * {@link HttpServletRequest}. For example, it may be stored in {@link HttpSession}.
 *
 * @see HttpSessionCsrfTokenRepository
 *
 * @author Rob Winch
 * @since 3.2
 *
 */
public interface CsrfTokenRepository {
	
	String CSRF_PARAMETER_NAME = "_csrf";
	String CSRF_HEADER_NAME = "X-CSRF-TOKEN";
	
	/**
	 * 生成token
	 */
	default CsrfToken generateToken(String value) {
		if (value == null) {
			value = UUID.randomUUID().toString();
		}
		return new DefaultCsrfToken(CSRF_HEADER_NAME, CSRF_PARAMETER_NAME, value);
	}
	
	/**
	 * Saves the {@link CsrfToken} using the {@link HttpServletRequest} and {@link HttpServletResponse}. If the
	 * {@link CsrfToken} is null, it is the same as deleting it.
	 *
	 * @param token the {@link CsrfToken} to save or null to delete
	 * @param request the {@link HttpServletRequest} to use
	 * @param response the {@link HttpServletResponse} to use
	 */
	void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * Loads the expected {@link CsrfToken} from the {@link HttpServletRequest}
	 *
	 * @param request the {@link HttpServletRequest} to use
	 * @return the {@link CsrfToken} or null if none exists
	 */
	CsrfToken loadToken(HttpServletRequest request);
}
