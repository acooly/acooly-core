/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月25日
 *
 */
package com.acooly.core.utils.lang;

import java.io.File;

import com.acooly.core.utils.Strings;

/**
 * @author zhangpu
 */
public class Paths {

	public static final String WEB_INF = "WEB-INF";

	public static String runtimePath(String path, final Class<?> clazz) {
		if (Strings.startsWith(path, "classpath:")) {
			return getClassepath(clazz) + trimStartSlash(Strings.substringAfter(path, "classpath:"));
		} else if (Strings.startsWith(path, "webroot:")) {
			return getWebroot(clazz) + trimStartSlash(Strings.substringAfter(path, "webroot:"));
		} else if (Strings.startsWith(path, "userhome:")) {
			return getUserhome() + "/" + trimStartSlash(Strings.substringAfter(path, "userhome:"));
		}
		return path;
	}

	public static String getClassepath(final Class<?> clazz) {
		Class<?> cz = clazz;
		if (cz == null) {
			cz = Paths.class;
		}
		return cz.getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	public static String getUserhome() {
		return Strings.trimToEmpty(System.getProperty("user.home"));
	}

	public static String getSysTmpDir() {
		return Strings.trimToEmpty(System.getProperty("java.io.tmpdir"));
	}

	public static String getWebroot(final Class<?> clazz) {
		String classpath = getClassepath(clazz);
		String webroot = null;

		// buid maven webroot, dev env
		if (Strings.contains(classpath, "target")) {
			String ifMavenWebroot = Strings.substringBeforeLast(classpath, "target") + "webapp";
			File file = new File(ifMavenWebroot);
			if (file.exists()) {
				return ifMavenWebroot;
			}
		}

		// runtime env
		if (Strings.contains(classpath, WEB_INF)) {
			webroot = Strings.trim(Strings.substringBefore(classpath, WEB_INF));
			if (Strings.isNotBlank(webroot)) {
				return webroot;
			}
		}

		String allClasspath = System.getProperty("java.class.path");
		if (Strings.isNotBlank(allClasspath) && Strings.contains(allClasspath, WEB_INF)) {
			String[] classpaths;
			if (Strings.contains(allClasspath, ":")) {
				classpaths = Strings.split(":"); // unix
			} else if (Strings.contains(allClasspath, ";")) {
				classpaths = Strings.split(";"); // windows
			} else {
				classpaths = new String[] { allClasspath };
			}
			if (classpaths != null && classpaths.length > 0) {
				for (String cp : classpaths) {
					if (Strings.contains(cp, WEB_INF)) {
						webroot = Strings.trim(Strings.substringBefore(cp, WEB_INF));
						break;
					}
				}
			}
			if (Strings.isNotBlank(webroot)) {
				return webroot;
			}
		}

		return Strings.trimToEmpty(webroot);

	}

	private static String trimStartSlash(String s) {
		if (s.startsWith("/") || s.startsWith("\\")) {
			s = s.substring(1);
		}
		return s;
	};

}
