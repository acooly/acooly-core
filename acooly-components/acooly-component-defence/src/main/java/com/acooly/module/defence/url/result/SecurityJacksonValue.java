package com.acooly.module.defence.url.result;

import com.acooly.module.defence.url.UrlSecurityService;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Data;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author qiubo@yiji.com
 */
@Data
public class SecurityJacksonValue {

    static UrlSecurityService urlSecurityService;
    static Jackson2ObjectMapperBuilder builder;
    private static ObjectMapper objectMapper;
    private Object object;

    @JsonRawValue
    @JsonValue
    public String json() {
        if (objectMapper == null) {
            synchronized (SecurityJacksonValue.class) {
                if (objectMapper == null) {
                    ObjectMapper objectMapper = builder.createXmlMapper(false).build();
                    SecurityBeanPropertyFilter securityBeanPropertyFilter = new SecurityBeanPropertyFilter();
                    securityBeanPropertyFilter.setUrlSecurityService(urlSecurityService);
                    objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
                    FilterProvider filters = new SimpleFilterProvider().addFilter("SecurityValue", securityBeanPropertyFilter)
                            .setFailOnUnknownId(false);
                    objectMapper.setFilterProvider(filters);
                    SecurityJacksonValue.objectMapper = objectMapper;
                }
            }
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFields(String[] fields) {
        SecurityContext.getContext().setFields(fields);
    }
}
