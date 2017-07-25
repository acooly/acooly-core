package com.acooly.module.defence.url.result;

import com.acooly.module.defence.url.SecurityParam;
import com.acooly.module.defence.url.UrlSecurityService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
/** @author qiubo@yiji.com */
@ControllerAdvice
public class SecurityResponseBodyAdvice implements ResponseBodyAdvice, InitializingBean {
	@Autowired
	private UrlSecurityService urlSecurityService;
	@Autowired
	private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;
	
	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return returnType.getMethod().getAnnotatedReturnType().isAnnotationPresent(SecurityParam.class);
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
		SecurityParam securityParam = returnType.getMethod().getAnnotatedReturnType()
			.getAnnotation(SecurityParam.class);
		SecurityJacksonValue securityJacksonValue = new SecurityJacksonValue();
		securityJacksonValue.setObject(body);
		securityJacksonValue.setFields(securityParam.value());
		return securityJacksonValue;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		SecurityJacksonValue.builder = jackson2ObjectMapperBuilder;
        SecurityJacksonValue.urlSecurityService=urlSecurityService;
	}
}
