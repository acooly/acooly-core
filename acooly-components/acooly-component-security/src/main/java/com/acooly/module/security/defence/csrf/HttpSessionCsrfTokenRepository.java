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

/**
 * A {@link CsrfTokenRepository} that stores the {@link CsrfToken} in the {@link HttpSession}.
 *
 * @author Rob Winch
 * @since 3.2
 */
public final class HttpSessionCsrfTokenRepository implements CsrfTokenRepository {

  private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME =
      HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");

  private String sessionAttributeName = DEFAULT_CSRF_TOKEN_ATTR_NAME;

  /*
   * (non-Javadoc)
   * @see org.springframework.security.web.csrf.CsrfTokenRepository#saveToken(org.springframework.security.web.csrf.CsrfToken, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
    if (token == null) {
      HttpSession session = request.getSession(false);
      if (session != null) {
        session.removeAttribute(sessionAttributeName);
      }
    } else {
      HttpSession session = request.getSession();
      session.setAttribute(sessionAttributeName, token);
    }
  }

  /* (non-Javadoc)
   * @see org.springframework.security.web.csrf.CsrfTokenRepository#loadToken(javax.servlet.http.HttpServletRequest)
   */
  public CsrfToken loadToken(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    return (CsrfToken) session.getAttribute(sessionAttributeName);
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.security.web.csrf.CsrfTokenRepository#generateToken(javax.servlet.http.HttpServletRequest)
   */

}
