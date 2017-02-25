/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 22:17 创建
 */
package com.acooly.module.appopenapi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static com.acooly.module.appopenapi.AppOpenapiProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
public class AppOpenapiProperties {
	public static final String PREFIX = "acooly.appopenapi";
	private boolean enable;
    private String anonymonsAccessKey;
    private String anonymonsSecretKey;
    private List<String> anonymonsService;
    /**
     * 设备绑定验证
     */
    private boolean deviceIdCheck=false;
    /**
     * 每次登陆动态生成秘钥，false表示登录后生成用户秘钥后不再改变
     */
    private boolean secretKeyDynamic=false;
	
}
