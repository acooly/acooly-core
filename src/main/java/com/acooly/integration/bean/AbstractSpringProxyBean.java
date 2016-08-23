/**
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.integration.bean;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.acooly.core.utils.GenericsUtils;

/**
 * Spring Proxy Bean 基类
 * <p/>
 * <li>T: 被代理接口
 * <li>D: 被代理接口的默认实现
 * 
 * @author zhangpu
 */
public abstract class AbstractSpringProxyBean<T, D> implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(AbstractSpringProxyBean.class);

	@Autowired
	protected ApplicationContext applicationContext;
	private T target;

	public T getTarget() {
		return target;
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getTargetInterface() {
		return GenericsUtils.getSuperClassGenricType(getClass(), 0);
	}

	@SuppressWarnings("unchecked")
	protected Class<D> getDefaultClass() {
		return GenericsUtils.getSuperClassGenricType(getClass(), 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Map<String, T> targets = applicationContext.getBeansOfType(getTargetInterface());
			this.target = (T) applicationContext.getBean(getDefaultClass());
			for (T t : targets.values()) {
				if (!t.getClass().equals(getDefaultClass()) && t != this) {
					this.target = t;
					break;
				}
			}
			logger.info("代理接口:{},实现:{}", getTargetInterface().getName(), target.getClass().getName());
		} catch (Exception e) {
			throw new RuntimeException("Spring容器自定义代理接口初始化失败", e);
		}

	}
}
