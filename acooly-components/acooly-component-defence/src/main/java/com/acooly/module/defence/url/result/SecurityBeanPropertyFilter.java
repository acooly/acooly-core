package com.acooly.module.defence.url.result;

import com.acooly.core.common.dao.support.EnhanceDefaultConversionService;
import com.acooly.module.defence.url.UrlSecurityService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
/** @author qiubo@yiji.com */
@Data
public class SecurityBeanPropertyFilter extends SimpleBeanPropertyFilter {
	private UrlSecurityService urlSecurityService;
    private EnhanceDefaultConversionService conversionService= EnhanceDefaultConversionService.INSTANCE;
	
	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider,
                                 PropertyWriter writer) throws Exception {
		String[] fields = SecurityContext.getContext().getFields();
		if (!ArrayUtils.contains(fields, writer.getName())) {
			writer.serializeAsField(pojo, jgen, provider);
			return;
		} else {
			jgen.writeFieldName(writer.getName());
			Object value = ((BeanPropertyWriter) writer).get(pojo);
			if (value == null) {
				jgen.writeNull();
			} else {
				if (conversionService.canConvert(value.getClass(), String.class)) {
					String strValue = conversionService.convert(value, String.class);
					if (strValue == null) {
						jgen.writeNull();
					} else {
						jgen.writeString(urlSecurityService.encrypt(strValue));
					}
				} else {
					throw new IllegalArgumentException(pojo.getClass() + "." + writer.getName() + "不能转换为String");
				}
				
			}
			
		}
	}
}
