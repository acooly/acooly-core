package com.acooly.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;

import com.acooly.core.utils.conversion.TypeConverterUtils;

public class ConfigurableConstants {
	protected static Logger logger = LoggerFactory.getLogger(ConfigurableConstants.class);
	protected static Properties p = new Properties();

	protected static void init(String propertyFileName) {
		InputStream in = null;
		try {
			in = ConfigurableConstants.class.getClassLoader().getResourceAsStream(propertyFileName);
			if (in != null) {
				p.load(in);
			}
			logger.info("load: {}", propertyFileName);
		} catch (IOException e) {
			logger.error("load " + propertyFileName + " into Constants error!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("close " + propertyFileName + " error!");
				}
			}
		}
	}

	/**
	 * 支持profile
	 * 
	 * @param propertyFileName
	 */
	protected static void initWithProfile(String propertyFileName) {
		String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		String configFile = propertyFileName;
		if (Strings.isNotBlank(activeProfile)) {
			configFile = Strings.substringBeforeLast(configFile, ".") + "." + activeProfile + "."
					+ Strings.substringAfterLast(configFile, ".");
		}
		init(configFile);
	}

	protected static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	/**
	 * 支持常见数据类型的转换
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T getProperty(String key, Class<T> clazz, T defaultValue) {
		String v = getProperty(key, "");
		if (Strings.isBlank(v)) {
			return defaultValue;
		}
		return (T) TypeConverterUtils.convertValue(v, clazz);
	}

}
