/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 22:17 创建
 */
package com.acooly.module.appopenapi;

import com.acooly.core.utils.validate.Validators;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.acooly.module.appopenapi.AppOpenapiProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class AppOpenapiProperties{
	public static final String PREFIX = "acooly.appopenapi";
	private boolean enable;
	/**
	 * 设备绑定验证
	 */
	private boolean deviceIdCheck = false;
	/**
	 * 每次登陆动态生成秘钥，false表示登录后生成用户秘钥后不再改变
	 */
	private boolean secretKeyDynamic = false;
	
	private Anonymous anonymous = new Anonymous();
	@Data
	public static class Anonymous {
        /**
         * 匿名accessKey
         */
        @NotBlank
		private String accessKey="anonymous";
        /**
         * 匿名secretKey
         */
        @NotBlank
        @Length(min = 16)
		private String secretKey="anonymouanonymou";
        /**
         * 匿名services
         */
        @NotNull
		private List<String> services= Lists.newArrayList();
	}
	@PostConstruct
    public void init(){
        this.getAnonymous().getServices().add("login");
        this.getAnonymous().getServices().add("bannerList");
        this.getAnonymous().getServices().add("appLatestVersion");
        this.getAnonymous().getServices().add("appCrashReport");
        this.getAnonymous().getServices().add("welcomeInfo");
        log.info("AppOpenapi匿名配置:{}",this.getAnonymous());
        Validators.assertJSR303(anonymous);
    }
}
