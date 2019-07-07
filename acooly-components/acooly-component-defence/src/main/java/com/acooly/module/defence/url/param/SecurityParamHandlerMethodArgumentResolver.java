package com.acooly.module.defence.url.param;

import com.acooly.core.common.dao.support.EnhanceDefaultConversionService;
import com.acooly.module.defence.url.SecurityParam;
import com.acooly.module.defence.url.UrlSecurityService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * @author qiubo@yiji.com
 */
public class SecurityParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UrlSecurityService urlSecurityService;
    private EnhanceDefaultConversionService conversionService = EnhanceDefaultConversionService.INSTANCE;
    private SecurityServletModelAttributeMethodProcessor modelAttributeMethodProcessor = new SecurityServletModelAttributeMethodProcessor(
            false);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SecurityParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        SecurityParam securityParam = parameter.getParameterAnnotation(SecurityParam.class);
        if (securityParam.value().length == 0) {
            String value = webRequest.getParameter(parameter.getParameterName());
            if (value == null) {
                return null;
            }
            Class<?> parameterType = parameter.getParameterType();
            String decrypted = urlSecurityService.decrypt(value);
            if (parameterType == String.class) {
                return decrypted;
            } else {
                if (conversionService.canConvert(String.class, parameterType)) {
                    return conversionService.convert(decrypted, parameterType);
                } else {
                    throw new IllegalArgumentException("不支持的参数类型:" + parameterType);
                }
            }
        } else {
            String[] value = securityParam.value();
            Map<String, String> map = Maps.newHashMap();
            for (String param : value) {
                if (Strings.isNullOrEmpty(param)) {
                    throw new IllegalArgumentException("不支持的参数类型:" + securityParam + " 不能有空属性名");
                }
                String v = webRequest.getParameter(param);
                if (v != null) {
                    String decrypted = urlSecurityService.decrypt(v);
                    map.put(param, decrypted);
                }
            }
            webRequest.setAttribute(SecurityParamHandlerMethodArgumentResolver.class.getName(), map, SCOPE_REQUEST);
            return modelAttributeMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        }

    }
}