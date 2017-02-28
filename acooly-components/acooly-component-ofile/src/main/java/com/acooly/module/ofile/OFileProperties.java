/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 16:11 创建
 */
package com.acooly.module.ofile;

import com.acooly.core.common.boot.Apps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.io.File;

import static com.acooly.module.ofile.OFileProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class OFileProperties implements InitializingBean {
	public static final String PREFIX = "acooly.ofile";
	private String serverRoot = "/media";
	private String storageRoot = "/data/media/";
	private String allowExtentions = "txt,zip,csv,xls,word,jpg,gif,png";
	private long maxSize = 5242880;
	private int thumbnailSize = 200;
	private boolean checkSession = false;
	private String checkSessionKey = "sessionCustomer,sessionUser,org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY";
	private boolean checkReferer = true;
	
	public String getStorageRoot() {
		return storageRoot;
	}
	
	public String getServerRoot() {
		return serverRoot;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(serverRoot);
		if (serverRoot.endsWith("/")) {
			serverRoot = serverRoot.substring(0, serverRoot.length());
		}
		Assert.notNull(storageRoot);
		if (SystemUtils.IS_OS_WINDOWS) {
		    if( !new File(storageRoot).isAbsolute()){
		        storageRoot=new File(storageRoot).getAbsolutePath();
            }
		    if(!storageRoot.contains(":")||!storageRoot.contains("\\")){
		        new File(storageRoot).getAbsolutePath();
                log.error("windows配置路径格式应该为:d:\\\\data\\\\projects");
                Apps.shutdown();
            }
            if (!storageRoot.endsWith("\\")) {
                storageRoot += "\\";
            }
		} else {
			if (!storageRoot.endsWith("/")) {
				storageRoot += "/";
			}
		}
	}
}
