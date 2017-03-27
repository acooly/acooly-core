/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-24 19:42 创建
 */
package com.acooly.module.test.param;

import junitparams.custom.ParametersProvider;
import junitparams.internal.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.runners.model.FrameworkMethod;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class CsvProvider implements ParametersProvider<CsvParameter> {
	private CsvParameter parameter;
	private DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
	private FrameworkMethod frameworkMethod;
	private boolean needConvert = false;
	private Class<?> parameterType = null;
	private ConversionService conversionService=new DefaultConversionService();
	
	public CsvProvider(FrameworkMethod frameworkMethod) {
		this.frameworkMethod = frameworkMethod;
		Class<?>[] parameterTypes = frameworkMethod.getMethod().getParameterTypes();
		if (parameterTypes.length > 1) {
			needConvert = false;
		} else {
			needConvert = true;
			parameterType = parameterTypes[0];
		}
	}
	
	@Override
	public void initialize(CsvParameter parameter) {
		this.parameter = parameter;
	}
	
	@Override
	public Object[] getParameters() {
		Reader reader = buildReader();
		BufferedReader br = new BufferedReader(reader);
		String line;
		List<Object> result = new LinkedList<>();
		int lineNo = 0;
		String[] header = null;
		try {
			while ((line = br.readLine()) != null) {

				lineNo++;
				if (lineNo == 1) {
					log.info("header:{}", line);
					header = Utils.splitAtCommaOrPipe(line);
				} else {
					result.add(parseLine(header, line, lineNo));
				}
				
			}
			return result.toArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(br);
		}
	}
	
	private Object parseLine(String[] header, String line, int lineNo) {
		if (!needConvert) {
			return line;
		} else {
			BeanWrapper beanWrapper = new BeanWrapperImpl(parameterType);
			beanWrapper.setConversionService(conversionService);
			String[] params = Utils.splitAtCommaOrPipe(line);
			if (params.length != header.length) {
				throw new RuntimeException("数据文件:" + parameter.value() + " 第" + lineNo + "行格式错误");
			}
			for (int i = 0; i < header.length; i++) {
				beanWrapper.setPropertyValue(header[i], params[i]);
			}
			return beanWrapper.getWrappedInstance();
		}
	}
	
	private Reader buildReader() {
		String filepath = parameter.value();
		Resource resource = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + filepath);
		if (resource.exists()) {
			try {
				return new InputStreamReader(resource.getInputStream(), "utf-8");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		} else {
			throw new RuntimeException("数据文件[" + filepath + "]不存在");
		}
		
	}
}
