package com.acooly.module.defence.url.param;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;
import java.util.Map;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
/** @author qiubo@yiji.com */
public class SecurityServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor {

	public SecurityServletModelAttributeMethodProcessor(boolean annotationNotRequired) {
		super(annotationNotRequired);
	}
	
	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		Map<String, String> maps = (Map<String, String>) request
			.getAttribute(SecurityParamHandlerMethodArgumentResolver.class.getName(), SCOPE_REQUEST);

		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
		ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        if(maps.isEmpty()){
            servletBinder.bind(servletRequest);
        }else {
            SecurityServletRequestWrapper securityServletRequestWrapper = new SecurityServletRequestWrapper(servletRequest,
                    maps);
            servletBinder.bind(securityServletRequestWrapper);
        }
	}
	
}
