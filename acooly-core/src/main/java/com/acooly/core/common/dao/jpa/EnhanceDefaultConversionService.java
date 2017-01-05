package com.acooly.core.common.dao.jpa;

import org.springframework.core.convert.support.DefaultConversionService;

public class EnhanceDefaultConversionService extends DefaultConversionService {

	public static EnhanceDefaultConversionService INSTANCE=new EnhanceDefaultConversionService();
	private EnhanceDefaultConversionService() {
		super();
		addConverter(new StringToDateConverter());
	}

}
