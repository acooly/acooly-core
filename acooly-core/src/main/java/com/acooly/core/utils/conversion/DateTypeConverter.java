package com.acooly.core.utils.conversion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link Date}的类型转换器。
 * 
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 * 
 */
public class DateTypeConverter extends AbstractTypeConverter<Date> {

	private static String MILLISECOND_FORMAT = ".SSS";

	public Class<Date> getTargetType() {
		return Date.class;
	}

	public List<Class<?>> getSupportedSourceTypes() {
		return Arrays.asList(CharSequence.class, String[].class, Date.class);
	}

	public Date convert(Object value, Class<? extends Date> toType) {
		try {
			return dateValue(value, toType);
		} catch (TypeConversionException e) {
			throw e;
		} catch (Exception e) {
			throw new TypeConversionException(e);
		}
	}

	public static Date dateValue(Object value, Class<? extends Date> toType) {
		Date result = null;
		if (value != null && value.getClass().isArray()
				&& String.class.isAssignableFrom(value.getClass().getComponentType())) {
			value = StringTypeConverter.stringValue(value);
		}
		if (value != null && value instanceof String && ((String) value).length() > 0) {
			String sa = (String) value;
			Locale locale = Locale.getDefault();

			DateFormat df = null;
			if (java.sql.Time.class == toType) {
				df = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
			} else if (java.sql.Timestamp.class == toType) {
				Date check;
				SimpleDateFormat dtfmt = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT,
						DateFormat.MEDIUM, locale);
				SimpleDateFormat fullfmt = new SimpleDateFormat(dtfmt.toPattern() + MILLISECOND_FORMAT, locale);

				SimpleDateFormat dfmt = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);

				SimpleDateFormat[] fmts = { fullfmt, dtfmt, dfmt };
				for (SimpleDateFormat fmt : fmts) {
					try {
						check = fmt.parse(sa);
						df = fmt;
						if (check != null) {
							break;
						}
					} catch (ParseException ignore) {
					}
				}
			} else if (Date.class == toType) {
				Date check = null;
				SimpleDateFormat d1 = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT,
						DateFormat.LONG, locale);
				SimpleDateFormat d2 = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT,
						DateFormat.MEDIUM, locale);
				SimpleDateFormat d3 = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT,
						DateFormat.SHORT, locale);
				SimpleDateFormat us = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // 美国语言的格式......
				SimpleDateFormat[] dfs = { d1, d2, d3, us };
				for (SimpleDateFormat df1 : dfs) {
					try {
						check = df1.parse(sa);
						df = df1;
						if (check != null) {
							break;
						}
					} catch (ParseException ignore) {
					}
				}
			}
			if (df == null) {
				df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			}
			try {
				df.setLenient(false);
				result = df.parse(sa);
				if (!(Date.class == toType)) {
					try {
						Constructor<? extends Date> constructor = toType.getConstructor(new Class[] { long.class });
						return constructor.newInstance(new Object[] { Long.valueOf(result.getTime()) });
					} catch (InvocationTargetException e) {
						throw new TypeConversionException(e.getTargetException());
					} catch (Exception e) {
						throw new TypeConversionException("没有默认的构造函数[default(long)]。", e);
					}
				}
			} catch (ParseException e) {
				throw new TypeConversionException("无法解析date", e);
			}
		} else if (Date.class.isInstance(value)) {
			result = (Date) value;
		}
		return result;
	}
}
