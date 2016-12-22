/**
 * create by zhangpu
 * date:2015年2月27日
 */
package com.acooly.module.security.defence.csrf;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.acooly.core.utils.Servlets;

/**
 * CSRF防御请求匹配
 * 
 * @author zhangpu
 *
 */
public class CsrfRequestMatcher implements RequestMatcher {
	private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
	PathMatcher pathMatcher = new AntPathMatcher();
	private List<String> allowedUrls;

	@Override
	public boolean matches(HttpServletRequest request) {
		return matcheUrl(request) || !allowedMethods.matcher(request.getMethod()).matches();
	}

	protected boolean matcheUrl(HttpServletRequest request) {
		boolean allowd = false;
		String requestUrl = Servlets.getRequestPath(request);
		if (allowedUrls != null && !allowedUrls.isEmpty()) {
			for (String allowedUrl : allowedUrls) {
				allowd = pathMatcher.match(allowedUrl, requestUrl);
				if (allowd) {
					break;
				}
			}
		}
		return allowd;
	}

	public void setAllowedUrls(List<String> allowedUrls) {
		this.allowedUrls = allowedUrls;
	}

}
