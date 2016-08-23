package com.acooly.core.common.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 基于Spring的单元测试基类（无事务）,名字短短更健康
 * 子类需要定义applicationContext文件的位置,如:@ContextConfiguration(locations = {
 * "/applicationContext-test.xml" }) ActiveProfiles指明使用测试profile
 * 
 * @author zhangpu
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-test-main.xml")
public abstract class SpringTests extends AbstractJUnit4SpringContextTests {

	static {
		System.setProperty("spring.profiles.active", "dev");
	}

	protected void simpleFillEntity(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
				continue;
			}
			String methodName = "set" + StringUtils.capitalize(field.getName());
			try {
				Method method = o.getClass().getMethod(methodName, field.getType());
				Random r = new Random();
				int value = r.nextInt(100);
				if ("String".equals(field.getType().getSimpleName())) {
					method.invoke(o, String.valueOf(value / 10));
				} else if ("Long".equals(field.getType().getSimpleName()) && !("id".equals(field.getName()))) {
					method.invoke(o, Long.valueOf(value));
				} else if ("Integer".equals(field.getType().getSimpleName()) && !("id".equals(field.getName()))) {
					method.invoke(o, Integer.valueOf(value));
				} else if ("Calendar".equals(field.getType().getSimpleName())) {
					method.invoke(o, Calendar.getInstance());
				} else if ("Date".equals(field.getType().getSimpleName())) {
					method.invoke(o, Calendar.getInstance().getTime());
				} else if ("Float".equals(field.getType().getSimpleName())) {
					method.invoke(o, Float.valueOf(value));
				}
			} catch (SecurityException e) {
				logger.warn("SecurityException: " + methodName + " method");
			} catch (NoSuchMethodException e) {
				logger.warn("NoSuchMethodException: " + methodName + " method");
			} catch (IllegalArgumentException e) {
				logger.warn("IllegalArgumentException: invoke method " + methodName + " ");
			} catch (IllegalAccessException e) {
				logger.warn("IllegalAccessException: invoke method " + methodName + " ");
			} catch (InvocationTargetException e) {
				logger.warn("InvocationTargetException: invoke method " + methodName + " ");
			}
		}
	}

}
