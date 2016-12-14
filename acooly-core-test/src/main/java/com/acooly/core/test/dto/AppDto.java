/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-13 15:24 创建
 */
package com.acooly.core.test.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author qiubo@yiji.com
 */
@Getter
@Setter
@ToString
public class AppDto {
	
	private String name;
	
	private String displayName;
	
	private String type;
	
	private Long userId;
	
	private Long parentAppId;
}
