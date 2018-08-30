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
import com.acooly.core.common.exception.AppConfigException;
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
    /**
     * 文件访问路径,可以配置为域名的形式，建议为//www.test.com/media,不区分协议
     */
    private String serverRoot = "/media";
    /**
     * 文件存储路径
     */
    private String storageRoot = "/data/media/";

    /**
     * 存储命名空间，默认为空，如果填写，文件存储路径会变为：storageRoot/storageNameSpace，如：/data/media/taodai
     */
    private String storageNameSpace;


    private String allowExtentions = "txt,zip,csv,xls,word,jpg,gif,png";
    private long maxSize = 5242880;
    private int thumbnailSize = 200;
    private boolean checkSession = false;
    private String checkSessionKey =
            "sessionCustomer,sessionUser,org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY";
    private boolean checkReferer = true;
    private boolean enableLocalMapping = true;

    /**
     * 可配置的内置文件上传签名认证器
     */
    private boolean configuredSignAuthEnable = false;
    /**
     * 可配置的内置文件上传签名认证器: 访问吗
     */
    private String configuredSignAuthAccessKey = "configuredSignAuthAccessKey";
    /**
     * 可配置的内置文件上传签名认证器: 安全码（秘钥）
     */
    private String configuredSignAuthSecretKey = "configuredSignAuthSecretKey";

    public String getStorageRoot() {
        return storageRoot;
    }

    public String getServerRoot() {
        return serverRoot;
    }

    String getServerRootMappingPath() {
        String path = "";
        boolean containDomain = false;
        if (serverRoot.startsWith("http://")) {
            path = serverRoot.substring(7);
            containDomain = true;
        }
        if (serverRoot.startsWith("https://")) {
            path = serverRoot.substring(8);
            containDomain = true;
        }
        if (serverRoot.startsWith("//")) {
            path = serverRoot.substring(2);
            containDomain = true;
        }
        if (containDomain) {
            int idx = path.indexOf('/');
            if (idx < 0) {
                if (enableLocalMapping) {
                    throw new AppConfigException("当启用本地tomcat映射时，必须指定访问域名路径，比如：http://res.example.com/media");
                }
            }
            path = path.substring(path.indexOf('/'));
        } else {
            path = serverRoot;
        }
        return path;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(serverRoot);
        if (serverRoot.endsWith("/")) {
            serverRoot = serverRoot.substring(0, serverRoot.length() - 1);
        }
        Assert.notNull(storageRoot);
        if (SystemUtils.IS_OS_WINDOWS) {
            if (!new File(storageRoot).isAbsolute()) {
                storageRoot = new File(storageRoot).getAbsolutePath();
            }
            if (!storageRoot.contains(":") || !storageRoot.contains("\\")) {
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
