package com.acooly.module.web.filter;

import com.google.common.base.Strings;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** @author qiubo@yiji.com */
public class HttpsOnlyFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (!isHttps(request)) {
      response.sendRedirect(buildHttpsUrl(request));
    } else {
      response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
      filterChain.doFilter(request, response);
    }
  }

  private boolean isHttps(HttpServletRequest request) {
    String protocol = String.valueOf(request.getHeader("X-Forwarded-Proto"));
    if (!Strings.isNullOrEmpty(protocol)) {
      return "https".equalsIgnoreCase(protocol);
    } else {
      return request.isSecure();
    }
  }

  private String buildHttpsUrl(HttpServletRequest request) {
    StringBuilder sb = new StringBuilder("https://");
    sb.append(request.getServerName());
    sb.append(request.getRequestURI());
    if (request.getQueryString() != null) {
      sb.append('?');
      sb.append(request.getQueryString());
    }
    return sb.toString();
  }

  @Override
  public void destroy() {}
}
