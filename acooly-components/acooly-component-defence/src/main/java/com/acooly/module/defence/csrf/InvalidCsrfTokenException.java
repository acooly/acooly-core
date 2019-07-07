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
package com.acooly.module.defence.csrf;

import javax.servlet.http.HttpServletRequest;

/**
 * Thrown when an expected {@link CsrfToken} exists, but it does not match the value present on the
 * {@link HttpServletRequest}
 *
 * @author Rob Winch
 * @since 3.2
 */
@SuppressWarnings("serial")
public class InvalidCsrfTokenException extends CsrfException {

    public InvalidCsrfTokenException(
            HttpServletRequest request, CsrfToken expectedAccessToken, String actualAccessToken) {
        super(
                "url:"
                        + buildFullRequestUrl(request)
                        + " , actualAccessToken:"
                        + actualAccessToken
                        + ", expectedAccessToken:"
                        + expectedAccessToken.getToken());
    }

    public static String buildFullRequestUrl(HttpServletRequest r) {
        return buildFullRequestUrl(
                r.getScheme(), r.getServerName(), r.getServerPort(), r.getRequestURI(), r.getQueryString());
    }

    /**
     * Obtains the full URL the client used to make the request.
     *
     * <p>Note that the server port will not be shown if it is the default server port for HTTP or
     * HTTPS (80 and 443 respectively).
     *
     * @return the full URL, suitable for redirects (not decoded).
     */
    public static String buildFullRequestUrl(
            String scheme, String serverName, int serverPort, String requestURI, String queryString) {

        scheme = scheme.toLowerCase();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        // Only add port if not default
        if ("http".equals(scheme)) {
            if (serverPort != 80) {
                url.append(":").append(serverPort);
            }
        } else if ("https".equals(scheme)) {
            if (serverPort != 443) {
                url.append(":").append(serverPort);
            }
        }

        // Use the requestURI as it is encoded (RFC 3986) and hence suitable for redirects.
        url.append(requestURI);

        if (queryString != null) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }
}
