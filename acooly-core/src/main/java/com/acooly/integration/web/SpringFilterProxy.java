/**
 * create by zhangpu
 * date:2015年2月27日
 */
package com.acooly.integration.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.acooly.core.utils.Servlets;

/**
 * Spring Filter代理
 * 
 * 扩展支持忽略URL
 * 
 * @author zhangpu
 *
 */
public class SpringFilterProxy extends DelegatingFilterProxy {
	PathMatcher pathMatcher = new AntPathMatcher();
	private List<String> exclusions = new ArrayList<String>();

	@Override
	protected void initFilterBean() throws ServletException {
		initExclusions();
		super.initFilterBean();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestUrl = Servlets.getRequestPath((HttpServletRequest) request);
		boolean isIgnore = false;
		if (!exclusions.isEmpty()) {
			for (String ignoreUrl : exclusions) {
				isIgnore = pathMatcher.match(ignoreUrl, requestUrl);
				if (isIgnore) {
					break;
				}
			}
		}
		if (isIgnore) {
			filterChain.doFilter(request, response);
			return;
		}
		super.doFilter(request, response, filterChain);
	}

	private void initExclusions() {
		String ignores = getFilterConfig().getInitParameter("exclusions");
		if (StringUtils.isNotBlank(ignores)) {
			String[] ignoreArray = ignores.split(",");
			for (int i = 0; i < ignoreArray.length; i++) {
				if (StringUtils.isNotBlank(ignoreArray[i])) {
					exclusions.add(ignoreArray[i]);
				}
			}
		}
		Collections.sort(exclusions);
		Collections.reverse(exclusions);
	}

}
