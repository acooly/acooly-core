/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 11:35 创建
 */
package com.acooly.module.mail;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.FreeMarkers;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@Service
public class MailTemplateService {
	@Autowired
	private MailProperties mailProperties;
	private Map<String, String> templates = Maps.newConcurrentMap();
	
	public String parse(String key, Map<String, String> data) {
		String template = getTemplates(key);
		return FreeMarkers.rendereString(template, data);
	}
	
	private String getTemplates(String key) {
		if (Apps.isDevMode()) {
			templates.clear();
		}
		String t = templates.get(key);
		if (Strings.isNullOrEmpty(t)) {
			String loc = mailProperties.getMailTemplatePath() + key + ".ftl";
			Resource resource = ApplicationContextHolder.get().getResource(loc);
			if (resource.exists()) {
				try {
					t = Resources.toString(resource.getURL(), Charsets.UTF_8);
					templates.put(key, t);
				} catch (IOException e) {
					throw new AppConfigException("邮件模板不存在:" + loc, e);
				}
			} else {
				throw new AppConfigException("邮件模板不存在:" + loc);
			}
		}
		return t;
	}
}
