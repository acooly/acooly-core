/**
 * create by zhangpu
 * date:2015年10月12日
 */
package com.acooly.integration.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.acooly.core.common.exception.AppConfigException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.google.common.collect.Lists;

/**
 * 静态资源请求处理器
 * 
 * 实现先加载webapp下资源,如果不存在再加载指定classpath下资源。用户封装静态资源到jar中，
 * 但外部项目任然可以通过在webapp下建同名资源文件覆盖扩展
 * 
 * 该特性使用了springmvc的HttpRequestHandler作为工具来实现请求资源的处理
 * 
 * @author zhangpu
 * @date 2015年10月12日
 */
public class StaticResourceHttpRequestHandler extends ResourceHttpRequestHandler {

	private String classpathViewRoot = "static/";
	private String webappViewRoot = "/";

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		List<Resource> locations = Lists.newArrayList();
		locations.add(new ServletContextResource(getServletContext(), this.webappViewRoot));
		locations.add(new ClassPathResource(this.classpathViewRoot));
		setLocations(locations);
	}

	@Override
	protected Resource getResource(HttpServletRequest request) {
		request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, request.getRequestURI());
		try {
			return super.getResource(request);
		} catch (IOException e) {
			throw new AppConfigException(e);
		}
	}

	public void setClasspathViewRoot(String classpathViewRoot) {
		this.classpathViewRoot = classpathViewRoot;
	}

	public void setWebappViewRoot(String webappViewRoot) {
		this.webappViewRoot = webappViewRoot;
	}

}
