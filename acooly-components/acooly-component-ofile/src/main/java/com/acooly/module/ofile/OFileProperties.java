/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 16:11 创建
 */
package com.acooly.module.ofile;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.module.ofile.OFileProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
public class OFileProperties {
	public static final String PREFIX = "acooly.ofile";
	private  String serverRoot = "/media";
	private  String storageRoot = "/data/media/";
	private String allowExtentions = "txt,zip,csv,xls,word,jpg,gif,png";
	private long maxSize = 5242880;
	private int thumbnailSize = 200;
	private boolean checkSession = false;
	private String checkSessionKey = "sessionCustomer,sessionUser,org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY";
	private boolean checkReferer = true;
}
