/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-02-18 17:51 创建
 */
package com.acooly.module.filterchain;

import com.acooly.core.utils.GenericsUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * 过滤器链默认实现类
 * @author qiubo@yiji.com
 */
public abstract class FilterChainBase<C extends Context> implements FilterChain<C>, ApplicationContextAware,
        InitializingBean, BeanNameAware {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected List<Filter<C>> filters = Lists.newArrayList();
	protected ApplicationContext applicationContext;
	protected String beanName;
	
	/**
	 * 执行filter链
	 * @param context 上下文对象
	 */
	@Override
	@SuppressWarnings("all")
	public void doFilter(C context) {
		if (context == null) {
			return;
		}
		if (context.iterator == null) {
			context.iterator = (Iterator) filters.iterator();
		}
		if (context.iterator.hasNext()) {
			Filter nextFilter = context.iterator.next();
			nextFilter.doFilter(context, this);
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	@SuppressWarnings("all")
	public void afterPropertiesSet() throws Exception {
		logger.info("FilterChain:{}初始化", beanName);
		Class<?> genricType = GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Filter.class, genricType);
		String[] beanNames = applicationContext.getBeanNamesForType(resolvableType);
		Assert.notEmpty(beanNames, this.getClass().getSimpleName() + " load  filters failed,cause of no Filter<"
									+ genricType.getSimpleName() + ">  found");
		for (String beanName : beanNames) {
			filters.add((Filter<C>) applicationContext.getBean(beanName));
		}
		OrderComparator.sort(filters);
        adjustFilters();
		filters.forEach(filter -> logger.info("加载filter:{}->{}", filter.getName(), filter.getClass().getName()));
	}

    protected void  adjustFilters(){

    }
	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}
}
