package com.acooly.core.common.dao.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.core.convert.converter.Converter;

/**
 * String to Date simple converter implement.
 * 
 * @author zhangpu
 */
public class StringToDateConverter implements Converter<String, Date> {

	public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_2 = "yyyy-MM-dd";
	public static final String DATE_FORMAT_3 = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_FORMAT_4 = "yyyy/MM/dd";

	public static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_1);

	private List<String> dateFormats = new ArrayList<String>();

	public StringToDateConverter() {
		super();
		addDefaultFormats();
	}
	@Override
	public Date convert(String source) {
		for (String dateFormat : dateFormats) {
			sdf.applyPattern(dateFormat);
			try {
				return sdf.parse(source);
			} catch (Exception e) {
				// ig
			}
		}
		throw new IllegalArgumentException(String.format("类型转换失败。支持格式：%s,但输入格式是[%s]", dateFormats.toString(), source));

	}

	private void addDefaultFormats() {
		dateFormats.add(DATE_FORMAT_1);
		dateFormats.add(DATE_FORMAT_2);
		dateFormats.add(DATE_FORMAT_3);
		dateFormats.add(DATE_FORMAT_4);
	}

}
